package com.undefined.akari.entity

import com.undefined.akari.AkariConfig
import com.undefined.akari.manager.NMSManager
import com.undefined.akari.player.CameraEntity
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

object NMSCamera : Camera {

    override fun createEntity(world: World): Any = NMSManager.nms.createItemDisplay(world)

    override fun createServerEntity(entity: Any, world: World): Any? = NMSManager.nms.createServerEntity(entity, world)

    override fun spawnForClient(entity: Any, serverEntity: Any?, player: Player) {
        NMSManager.nms.sendSpawnPacket(entity, serverEntity, listOf(player))
    }

    override fun spawn(world: World, location: Location, players: List<Player>): CameraEntity {
        val entity = createEntity(world)
        NMSManager.nms.setEntityLocation(entity, location)
        val serverEntity = createServerEntity(entity, world)
        players.forEach { spawnForClient(entity, serverEntity, it) }
        return CameraEntity(entity, serverEntity)
    }

    override fun setInterpolationDuration(
        entity: Any,
        duration: Int,
        players: List<Player>
    ) {
        NMSManager.nms.setInterpolationDuration(entity, duration, players)
    }

    override fun setCamera(entity: Any, players: List<Player>) {
        NMSManager.nms.sendSetCameraPacket(entity, players)
    }

    override fun removeCamera(players: List<Player>) {
        NMSManager.nms.sendRemoveCameraPacket(players)
    }

    override fun teleport(entity: Any, location: Location, players: List<Player>) {
        CompletableFuture.supplyAsync {
            NMSManager.nms.setEntityLocation(entity, location)
            NMSManager.nms.sendTeleportPacket(entity, players)
        }
    }

    override fun kill(entity: Any, players: List<Player>) {
        NMSManager.nms.sendRemoveEntityPacket(entity, players)
    }
}