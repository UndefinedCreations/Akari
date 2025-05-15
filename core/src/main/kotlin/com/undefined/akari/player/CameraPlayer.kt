@file:Suppress("MemberVisibilityCanBePrivate")

package com.undefined.akari.player

import com.undefined.akari.entity.BukkitCamera
import com.undefined.akari.entity.Camera
import com.undefined.akari.entity.NMSCamera
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player

class CameraPlayer(
    var playingWorld: World
) {

    var cameraSequence: CameraSequence? = null
    var looping: Boolean = false

    private val players: PlayerList by PlayerList(this, mutableListOf())
    internal var active: Boolean = false

    internal var cameraEntity: CameraEntity? = null

    var camera: Camera = NMSCamera

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

    fun start(player: List<Player>, playingWorld: World? = null) {
        if(cameraSequence == null) throw IllegalArgumentException("No CameraSequence set")

        val playingWorld = playingWorld ?: this.playingWorld

        this.cameraEntity = camera.spawn(playingWorld, )

        startPlayingLoop()

        this.active = true
    }

    private fun spawnEntities(world: World, firstLocation: Location, players: List<Player>) {
        val itemDisplay = camera.spawn(world, firstLocation, players)
    }

    private fun startPlayingLoop() {



    }

}