package com.undefined.akari.nms

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.joml.Quaternionf
import org.joml.Vector3f

interface NMS {
    fun createItemDisplay(world: World): Any

    fun setEntityLocation(entity: Any, location: Location)

    fun createServerEntity(entity: Any, world: World): Any?

    fun sendSpawnPacket(entity: Any, serverEntity: Any?, players: List<Player>)

    fun sendRemoveEntityPacket(entity: Any, players: List<Player>)

    fun sendSetCameraPacket(entity: Any, players: List<Player>)

    fun sendRemoveCameraPacket(players: List<Player>)

    fun setInterpolationDuration(entity: Any, interpolationDuration: Int, players: List<Player>)

    fun sendTeleportPacket(entity: Any, players: List<Player>)

    fun createBlockDisplay(world: World): Any

    fun setBlockDisplayBlock(entity: Any, blockData: BlockData)

    fun setTransformation(entity: Any, translation: Vector3f, leftRotation: Quaternionf, scale: Vector3f, rightRotation: Quaternionf)

    fun sendEntityData(display: Any, toList: List<Player>)
    fun setItemDisplayItem(entity: Any, itemStack: ItemStack)
}