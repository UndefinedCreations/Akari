package com.undefined.akari.entity

import com.undefined.akari.player.CameraEntity
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player

object BukkitCamera : Camera {
    override fun createEntity(world: World): Any = "BUKKIT ENTITY DOESN'T NEED THIS"

    override fun createServerEntity(entity: Any, world: World): Any? = "BUKKIT ENTITY DOESN'T NEED THIS"

    override fun spawnForClient(entity: Any, serverEntity: Any?, player: Player) {}

    override fun spawn(
        world: World,
        location: Location,
        players: List<Player>
    ): CameraEntity {
        return CameraEntity(world.spawn(location, ItemDisplay::class.java), null)
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