package com.undefined.akari.nms

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.util.Vector

interface NMS {
    fun createItemDisplay(world: World): Any

    fun createTextDisplay(world: World): Any

    fun setTextTextDisplay(display: Any, text: String, players: List<Player>)

    fun setScale(display: Any, scale: Vector, players: List<Player>)

    fun setTransformation(display: Any, vector: Vector, players: List<Player>)

    fun setTransformationInterpolationDuration(display: Any, interpolationDuration: Int, players: List<Player>)

    fun addPassenger(camera: Any, display: Any, players: List<Player>)

    fun setEntityLocation(entity: Any, location: Location)

    fun createServerEntity(entity: Any, world: World): Any?

    fun sendSpawnPacket(entity: Any, serverEntity: Any?, players: List<Player>)

    fun sendRemoveEntityPacket(entity: Any, players: List<Player>)

    fun sendSetCameraPacket(entity: Any, players: List<Player>)

    fun sendRemoveCameraPacket(players: List<Player>)

    fun setInterpolationDuration(entity: Any, interpolationDuration: Int, players: List<Player>)

    fun sendTeleportPacket(entity: Any, players: List<Player>)
}