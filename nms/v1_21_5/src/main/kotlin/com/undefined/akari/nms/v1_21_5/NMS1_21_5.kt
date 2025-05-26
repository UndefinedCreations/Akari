@file:Suppress("NAME_SHADOWING")

package com.undefined.akari.nms.v1_21_5

import com.google.common.collect.ImmutableList
import com.undefined.akari.nms.NMS
import com.undefined.akari.util.getPrivateField
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket
import net.minecraft.network.protocol.game.ClientboundSetCameraPacket
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializer
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerEntity
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.Display.ItemDisplay
import net.minecraft.world.entity.Display.TextDisplay
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.PositionMoveRotation
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.craftbukkit.v1_21_R4.CraftWorld
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import org.joml.Vector3f

object NMS_1_21_5 : NMS {

    object Mapping {
        const val SET_ROTATION = "b"

        const val BILLBAORD_ID = "g"
    }

    override fun createItemDisplay(world: World): Any =
        ItemDisplay(EntityType.ITEM_DISPLAY, (world as CraftWorld).handle)


    override fun createTextDisplay(world: World): Any =
        TextDisplay(EntityType.TEXT_DISPLAY, (world as CraftWorld).handle)

    override fun setTextTextDisplay(display: Any, text: String, players: List<Player>) {
        val textDisplay = display as? TextDisplay ?: return
        val dataList = mutableListOf(SynchedEntityData.DataValue.create(EntityDataAccessor(23, EntityDataSerializers.COMPONENT),
            Component.literal(text)))
        players.sendPackets(ClientboundSetEntityDataPacket(textDisplay.id, dataList as List<SynchedEntityData.DataValue<*>>))
    }

    override fun setScale(display: Any, scale: Vector, players: List<Player>) {
        val display = display as? Display ?: return
        sendMetaData(
            display.id,
            12,
            EntityDataSerializers.VECTOR3,
            Vector3f(
                scale.x.toFloat(),
                scale.y.toFloat(),
                scale.z.toFloat()
            ), players
        )
    }

    override fun setTransformation(display: Any, vector: Vector, players: List<Player>) {
        val display = display as? Display ?: return
        sendMetaData(
            display.id,
            11,
            EntityDataSerializers.VECTOR3,
            Vector3f(
                vector.x.toFloat(),
                vector.y.toFloat(),
                vector.z.toFloat()
            ),
            players
            )
    }

    override fun setTransformationInterpolationDuration(
        display: Any,
        interpolationDuration: Int,
        players: List<Player>
    ) {
        val display = display as? Display ?: return
        sendMetaData(
            display.id,
            9,
            EntityDataSerializers.INT,
            interpolationDuration,
            players
        )
    }

    override fun setBillboard(
        display: Any,
        billboard: org.bukkit.entity.Display.Billboard,
        players: List<Player>
    ) {
        val display = display as? Display ?: return
        val nmsBillboard = Display.BillboardConstraints.valueOf(billboard.name)
        sendMetaData(display.id, 15, EntityDataSerializers.BYTE, nmsBillboard.getPrivateField(Mapping.BILLBAORD_ID), players)
    }

    override fun addPassenger(camera: Any, display: Any, players: List<Player>) {
        val camera = camera as? ItemDisplay ?: return
        val display = display as? Display ?: return

        if (camera.passengers.isEmpty()) {
            camera.passengers = ImmutableList.of(display)
        } else {
            val passengerList: MutableList<Entity> = camera.passengers.toMutableList()
            passengerList.add(camera)
            camera.passengers = ImmutableList.copyOf(passengerList)
        }



        players.sendPackets(ClientboundSetPassengersPacket(camera))
    }

    override fun setEntityLocation(entity: Any, location: Location) {
        val entity = entity as? Entity ?: return
        entity.setPos(location.x, location.y, location.z)
        Entity::class.java.getDeclaredMethod(Mapping.SET_ROTATION, Float::class.java, Float::class.java).apply {
            this.isAccessible = true
        }(entity, location.yaw, location.pitch)
    }

    override fun createServerEntity(entity: Any, world: World): Any =
        ServerEntity(
            (world as CraftWorld).handle,
            entity as Entity,
            0,
            false,
            {},
            { _, _ -> },
            mutableSetOf()
        )

    override fun sendSpawnPacket(entity: Any, serverEntity: Any?, players: List<Player>) {
        val entity = entity as? Entity ?: return
        println("Spawning")
        players.sendPackets(ClientboundAddEntityPacket(entity, (serverEntity as ServerEntity)))
    }

    override fun sendRemoveEntityPacket(entity: Any, players: List<Player>) {
        val entity = entity as? Entity ?: return
        players.sendPackets(ClientboundRemoveEntitiesPacket(entity.id))
    }

    override fun sendSetCameraPacket(entity: Any, players: List<Player>) {
        val entity = entity as? ItemDisplay ?: return
        players.sendPackets(ClientboundSetCameraPacket(entity))
    }

    override fun sendRemoveCameraPacket(players: List<Player>) {
        players.forEach {
            it.sendPackets(ClientboundSetCameraPacket(it.serverPlayer()))
        }
    }

    override fun setTextAlignment(
        display: Any,
        alignment: org.bukkit.entity.TextDisplay.TextAlignment,
        players: List<Player>
    ) {
        val display = display as? Display ?: return

    }

    override fun setInterpolationDuration(entity: Any, interpolationDuration: Int, players: List<Player>) {
        val entity = entity as? Display ?: return
        sendMetaData(entity.id, 10, EntityDataSerializers.INT, interpolationDuration, players)
    }

    override fun sendTeleportPacket(entity: Any, players: List<Player>) {
        val entity = entity as? Display ?: return
        players.sendPackets(ClientboundTeleportEntityPacket(entity.id, PositionMoveRotation.of(entity), setOf(), false))
    }

    private fun <T> sendMetaData(entityId: Int, accessorId: Int, entityDataSerializers: EntityDataSerializer<T>, data: T, players: List<Player>) {
        val dataList = mutableListOf(SynchedEntityData.DataValue.create(EntityDataAccessor(accessorId, entityDataSerializers), data))
        players.sendPackets(ClientboundSetEntityDataPacket(entityId, dataList as List<SynchedEntityData.DataValue<*>>))
    }

    private fun Player.serverPlayer(): ServerPlayer = (this as CraftPlayer).handle

    private fun Player.sendPackets(vararg packets: Packet<*>?) {
        val connection = serverPlayer().connection
        packets.filterNotNull().forEach { connection.sendPacket(it) }
    }

    private fun Collection<Player>.sendPackets(vararg packet: Packet<*>?) {
        for (player in this) {
            player.sendPackets(*packet)
        }
    }

}