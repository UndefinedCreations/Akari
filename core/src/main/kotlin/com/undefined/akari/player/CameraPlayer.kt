@file:Suppress("MemberVisibilityCanBePrivate")

package com.undefined.akari.player

import com.undefined.akari.AkariConfig
import com.undefined.akari.entity.BukkitCamera
import com.undefined.akari.entity.Camera
import com.undefined.akari.entity.NMSCamera
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import kotlin.math.floor

class CameraPlayer(
    var playingWorld: World,
    private val kotlinDSL: CameraPlayer.() -> Unit = {}
) {

    var cameraSequence: CameraSequence? = null
    var looping: Boolean = false

    private val players: PlayerList by PlayerList(this, mutableListOf())
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

    fun addPlayer(player: Player) = apply { addPlayers(listOf(player)) }

    fun addPlayers(players: List<Player>) = apply { this.players.addAll(players) }

    fun kick(player: Player) = apply { kick(listOf(player)) }

    fun kick(players: List<Player>) = apply { this.players.removeAll(players) }

    fun start(player: Player) = start(listOf(player))

    fun start(players: List<Player>, playingWorld: World? = null) = apply {
        if(cameraSequence == null) throw IllegalArgumentException("No CameraSequence set")

        val rate = floor(tickRate / 20.0)

        val playingWorld = playingWorld ?: this.playingWorld

        this.players.addAll(players)
        this.cameraEntity = camera.spawn(playingWorld, cameraSequence!!.firstLocation(playingWorld), players)
        camera.setInterpolationDuration(cameraEntity!!.entity, rate.toInt(), players)
        camera.setCamera(cameraEntity!!.entity, players)
        startPlayingLoop(playingWorld, rate.toInt())

        this.active = true
    }

    fun stop() = apply {
        cameraEntity?.run { camera.kill(this.entity, players) }
        cancelTask()
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
                camera.teleport(cameraEntity!!.entity, nextCameraPoint.toLocation(playingWorld), players)
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