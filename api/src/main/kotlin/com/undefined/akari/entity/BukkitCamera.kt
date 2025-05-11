package com.undefined.akari.entity

import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player

object BukkitCamera : Camera {

    override fun spawn(
        world: World,
        location: Location,
        players: List<Player>
    ): Any {
        return world.spawn(location, ItemDisplay::class.java)
    }

    override fun setInterpolationDuration(
        entity: Any,
        duration: Int,
        players: List<Player>
    ) {
        (entity as? ItemDisplay)?.teleportDuration = 1
    }

    override fun setCamera(entity: Any, players: List<Player>) {
        (entity as? ItemDisplay)?.let { entity ->
            players.forEach {
                it.gameMode = GameMode.SPECTATOR
                it.spectatorTarget = entity
            }
        }
    }

    override fun removeCamera(players: List<Player>) {
        players.forEach { it.spectatorTarget = it }
    }

    override fun teleport(
        entity: Any,
        location: Location,
        players: List<Player>
    ) {
        (entity as? ItemDisplay)?.teleport(location)
    }

    override fun kill(entity: Any, players: List<Player>) {
        (entity as? ItemDisplay)?.remove()
    }
}