package com.undefined.akari.entity

import com.undefined.akari.manager.NMSManager
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player

object NMSCamera : Camera {

    override fun spawn(world: World, location: Location, players: List<Player>): Any {
        val entity = NMSManager.nms.createItemDisplay(world)
        NMSManager.nms.setEntityLocation(entity, location)
        val serverEntity = NMSManager.nms.createServerEntity(entity, world)
        NMSManager.nms.sendSpawnPacket(entity, serverEntity, players)
        return entity
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
        NMSManager.nms.setEntityLocation(entity, location)
        NMSManager.nms.sendTeleportPacket(entity, players)
    }

    override fun kill(entity: Any, players: List<Player>) {
        NMSManager.nms.sendRemoveEntityPacket(entity, players)
    }
}