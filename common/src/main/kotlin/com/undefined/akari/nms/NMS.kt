package com.undefined.akari.nms

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player

interface NMS {

    fun sendCreateEntity(world: World): Any

    fun setEntityLocation(entity: Any, location: Location)

    fun createServerEntity(entity: Any, world: World): Any?

    fun sendSpawnPackets(entity: Any, serverEntity: Any, players: List<Player>)

    fun sendRemoveEntityPacket(entity: Any, players: List<Player>)

    fun sendSetCameraPacket(entity: Any, players: List<Player>)

    fun sendRemoveCameraPacket(players: List<Player>)

    fun setInterpolationDuration(entity: Any, interpolationDuration: Int, players: List<Player>)
}