package com.undefined.akari.v1_21_3

import com.undefined.akari.entity.CamaraDisplay
import com.undefined.akari.objects.CamaraPoint
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket
import net.minecraft.network.protocol.game.ClientboundSetCameraPacket
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerEntity
import net.minecraft.world.entity.Display.ItemDisplay
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.PositionMoveRotation
import org.bukkit.craftbukkit.CraftWorld
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player

class CamaraDisplay: CamaraDisplay {

    private val players: MutableList<Player> = mutableListOf()

    private var entity: ItemDisplay? = null


    override fun spawn(cPoint: CamaraPoint, players: Collection<Player>) {
        this.players.addAll(players)

        //Getting Entity Class
        val craftWorld = cPoint.world as CraftWorld
        val entity = ItemDisplay(EntityType.ITEM_DISPLAY, craftWorld.handle)

        setEntityLocation(entity, cPoint)

        //Getting serverEntity
        val serverEntity = ServerEntity(
            craftWorld.handle,
            entity,
            0,
            false,
            {},
            mutableSetOf()
        )

        //Add Entity Packet
        val addEntityPacket = entity.getAddEntityPacket(serverEntity)

        //Sends packet
        players.sendPacket(addEntityPacket)

        this.entity = entity
    }

    override fun remove() {
        if (entity == null) return
        players.sendPacket(ClientboundRemoveEntitiesPacket(entity!!.id))
        players.clear()
    }

    override fun teleport(cPoint: CamaraPoint) {
        if (entity == null) return

        setEntityLocation(entity!!, cPoint)

        players.sendPacket(ClientboundTeleportEntityPacket.teleport(entity!!.id, PositionMoveRotation.of(entity!!), setOf(), false))
    }

    override fun setViewer(player: Player) {
        if (entity == null) return

        players.sendPacket(ClientboundSetCameraPacket(entity!!))
    }

    override fun removeViewers(players: List<Player>) {
        players.forEach {
            it.sendPacket(ClientboundSetCameraPacket((it as CraftPlayer).handle))
        }
    }

    override fun interpolationDuration(interpolatorDuration: Int) {
        if (entity == null) return

        val dataList = mutableListOf(SynchedEntityData.DataValue.create(EntityDataAccessor(10, EntityDataSerializers.INT), interpolatorDuration))

        players.sendPacket(ClientboundSetEntityDataPacket(entity!!.id, dataList as List<SynchedEntityData.DataValue<*>>))
    }

    private fun setEntityLocation(entity: Entity, cPoint: CamaraPoint) {
        //Setting Location
        entity.setPos(cPoint.x, cPoint.y, cPoint.z)

        val setRotationMethod = Entity::class.java.getDeclaredMethod(
            Mappings.Entity.Method.SET_ROTATION,
            Float::class.java,
            Float::class.java
        )
        setRotationMethod.isAccessible = true
        setRotationMethod.invoke(entity, cPoint.yaw, cPoint.pitch)
    }

    private fun Collection<Player>.sendPacket(vararg packet: Packet<*>) = forEach { it.sendPacketArray(packet) }

    private fun Player.sendPacket(packet: Packet<*>) {
        val craftPlayer = this as CraftPlayer
        val serverPlayer = craftPlayer.handle
        val connection = serverPlayer.connection
        connection.sendPacket(packet)
    }

    private fun Player.sendPacketArray(packet: Array<out Packet<*>>) = packet.forEach {
        this.sendPacket(it)
    }

}