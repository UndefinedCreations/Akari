package com.undefined.akari.entity

import com.undefined.akari.player.CameraEntity
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player
import kotlin.time.Duration

interface Camera {

    fun createEntity(world: World): Any

    fun createServerEntity(entity: Any, world: World): Any?

    fun spawnForClient(entity: Any, serverEntity: Any? , player: Player)

    fun spawn(world: World, location: Location, players: List<Player>): CameraEntity

    fun setInterpolationDuration(entity: Any, duration: Int, players: List<Player>)

    fun setCamera(entity: Any, players: List<Player>)

    fun removeCamera(players: List<Player>)

    fun teleport(entity: Any, location: Location, players: List<Player>)

    fun kill(entity: Any, players: List<Player>)

}