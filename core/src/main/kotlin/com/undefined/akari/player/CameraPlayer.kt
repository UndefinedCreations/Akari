@file:Suppress("MemberVisibilityCanBePrivate")

package com.undefined.akari.player

import com.undefined.akari.AkariConfig
import com.undefined.akari.Options
import com.undefined.akari.entity.BukkitCamera
import com.undefined.akari.entity.Camera
import com.undefined.akari.entity.NMSCamera
import com.undefined.akari.events.CameraStartEvent
import com.undefined.akari.events.CameraStopEvent
import com.undefined.akari.events.PlayerCameraAddEvent
import com.undefined.akari.events.PlayerCameraKickEvent
import com.undefined.akari.manager.PlayerManager
import org.bukkit.GameMode
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import kotlin.math.floor

class CameraPlayer(
    var playingWorld: World,
    kotlinDSL: CameraPlayer.() -> Unit = {}
) {

    var cameraSequence: CameraSequence? = null
    var looping: Boolean = false

    val players: PlayerList by PlayerList(this, mutableSetOf())
    internal var active: Boolean = false

    internal var cameraEntity: CameraEntity? = null

    var camera: Camera = NMSCamera
    private var bukkitTask: BukkitTask? = null

    var exitGameMode: GameMode? = null

    var tickRate: Int = 20
        set(value) {
            if (value > 0 ) field = value
        }

    init {
        kotlinDSL(this)
    }


    fun setExitGameMode(gameMode: GameMode?) = apply {
        this.exitGameMode = gameMode
    }

    fun setTickRate(tickRate: Int) = apply {
        if (tickRate > 0) this.tickRate = tickRate
    }

    fun setPlayingWorld(world: World) = apply {
        this.playingWorld = world
    }

    fun setBukkitCamera(bukkit: Boolean) = apply {
        camera = if (bukkit) BukkitCamera else NMSCamera
    }

    fun setCameraSequence(cameraSequence: CameraSequence) = apply {
        this.cameraSequence = cameraSequence
    }

    fun setLooping(looping: Boolean) = apply {
        this.looping = looping
    }

    fun getPlayers (): List<Player> = players.toList()

    fun addPlayer(player: Player) = apply {
        val addEvent = PlayerCameraAddEvent(this).also { it.call() }
        if (addEvent.isCancelled) return@apply

        if (Options.kickOnMultiple) {
            if (PlayerManager.getPlayer(player) != null) {
                PlayerManager.getPlayer(player)!!.kick(player)
            }
        }

        this.players.add(player)
    }

    fun isActive (): Boolean {
        return active
    }

    fun addPlayers(players: List<Player>) = apply {
        players.forEach { addPlayer(it) }
    }

    fun kick(player: Player) = apply {

        val kickEvent = PlayerCameraKickEvent(this).also { it.call() }
        if (kickEvent.isCancelled) return@apply

        players.remove(player)
    }

    fun kick(players: List<Player>) = apply { players.forEach { kick(it) } }

    fun start(player: Player) = start(listOf(player))

    fun start(players: List<Player> = listOf(), playingWorld: World? = null) = apply {
        if(cameraSequence == null) throw IllegalArgumentException("No CameraSequence set")

        val startEvent = CameraStartEvent(this).also { it.call() }
        if (startEvent.isCancelled) return@apply

        PlayerManager.addPlayer(this)

        val rate = floor(tickRate / 20.0)

        val playingWorld = playingWorld ?: this.playingWorld

        addPlayers(players)
        this.cameraEntity = camera.spawn(playingWorld, cameraSequence!!.firstLocation(playingWorld), players)
        camera.setInterpolationDuration(cameraEntity!!.entity, rate.toInt(), players)
        camera.setCamera(cameraEntity!!.entity, players)
        startPlayingLoop(playingWorld, rate.toInt())

        this.active = true
    }

    fun stop() = apply {

        CameraStopEvent(this).call()

        cameraEntity?.run { camera.kill(this.entity, players.toList()) }
        cancelTask()

        active = false

        PlayerManager.removePlayer(this)
    }

    private fun startPlayingLoop(playingWorld: World, tickRate: Int) {

        val path = cameraSequence!!.pathMap.values.flatMap { it.calculatedPoints.values }
        var index = 0

        bukkitTask = object : BukkitRunnable() {
            override fun run() {
                if (index >= path.size && !looping) {
                    stop()
                    return
                } else if (index >= path.size) index = 0
                val nextCameraPoint = path[index]
                camera.teleport(cameraEntity!!.entity, nextCameraPoint.toLocation(playingWorld), players.toList())
                index++
            }
        }.runTaskTimer(AkariConfig.javaPlugin, 0, tickRate.toLong())
    }

    private fun cancelTask() {
        if (bukkitTask == null) return
        bukkitTask!!.cancel()
        bukkitTask = null
        players.clear()
    }

}