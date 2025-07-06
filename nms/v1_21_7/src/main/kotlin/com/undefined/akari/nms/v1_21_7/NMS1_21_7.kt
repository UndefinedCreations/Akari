@file:Suppress("NAME_SHADOWING")

package com.undefined.akari.nms.v1_21_7

import com.undefined.akari.nms.NMS
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket
import net.minecraft.network.protocol.game.ClientboundSetCameraPacket
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerEntity
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.PositionMoveRotation
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_21_R5.CraftWorld
import org.bukkit.entity.Player

object NMS_1_21_7 : NMS {

    object Mapping {
        const val SET_ROTATION = "b"
    }

    override fun createItemDisplay(world: World): Any =
        Display.ItemDisplay(EntityType.ITEM_DISPLAY, (world as CraftWorld).handle)

    override fun setEntityLocation(entity: Any, location: Location) {
        val entity = entity as? Display.ItemDisplay ?: return
        entity.setPos(location.x, location.y, location.z)

        Entity::class.java.getDeclaredMethod(Mapping.SET_ROTATION, Float::class.java, Float::class.java).apply {
            this.isAccessible = true
        }(entity, location.yaw, location.pitch)
    }

    override fun createServerEntity(entity: Any, world: World): Any =
        ServerEntity(
            (world as CraftWorld).handle,
            entity as Display.ItemDisplay,
            0,
            false,
            {},
            { _, _ -> },
            mutableSetOf()
        )

    override fun sendSpawnPacket(entity: Any, serverEntity: Any?, players: List<Player>) {
        val entity = entity as? Display.ItemDisplay ?: return
        players.sendPackets(ClientboundAddEntityPacket(entity, (serverEntity as ServerEntity)))
    }

    override fun sendRemoveEntityPacket(entity: Any, players: List<Player>) {
        val entity = entity as? Display.ItemDisplay ?: return
        players.sendPackets(ClientboundRemoveEntitiesPacket(entity.id))
    }

    override fun sendSetCameraPacket(entity: Any, players: List<Player>) {
        val entity = entity as? Display.ItemDisplay ?: return
        players.sendPackets(ClientboundSetCameraPacket(entity))
    }

    override fun sendRemoveCameraPacket(players: List<Player>) {
        players.forEach {
            it.sendPackets(ClientboundSetCameraPacket(it.serverPlayer()))
        }
    }

    override fun setInterpolationDuration(entity: Any, interpolationDuration: Int, players: List<Player>) {
        val entity = entity as? Display.ItemDisplay ?: return
        val dataList = mutableListOf(SynchedEntityData.DataValue.create(EntityDataAccessor(10, EntityDataSerializers.INT), interpolationDuration))
        players.sendPackets(ClientboundSetEntityDataPacket(entity.id, dataList as List<SynchedEntityData.DataValue<*>>))
    }

    override fun sendTeleportPacket(entity: Any, players: List<Player>) {
        val entity = entity as? Display.ItemDisplay ?: return
        players.sendPackets(ClientboundTeleportEntityPacket(entity.id, PositionMoveRotation.of(entity), setOf(), false))
    }

    private fun Player.serverPlayer(): ServerPlayer = (this as CraftPlayer).handle

    private fun Player.sendPackets(vararg packets: Packet<*>?) {
        val connection = serverPlayer().connection
        packets.filterNotNull().forEach { connection.send(it) }
    }

    private fun Collection<Player>.sendPackets(vararg packet: Packet<*>?) {
        for (player in this) {
            player.sendPackets(*packet)
        }
    }

}