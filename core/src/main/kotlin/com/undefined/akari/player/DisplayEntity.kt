package com.undefined.akari.player

import com.undefined.akari.display.DisplayElement
import com.undefined.akari.manager.NMSManager
import org.bukkit.entity.Display
import org.bukkit.entity.Player
import org.bukkit.util.Vector

data class DisplayEntity(
    val akariEntity: AkariEntity,
    var vector: Vector
) {

    fun spawn(players: List<Player>, camera: Any, vector: Vector, displayEntity: DisplayElement<*>) {
        NMSManager.nms.sendSpawnPacket(akariEntity.entity, akariEntity.serverEntity, players)
        NMSManager.nms.addPassenger(camera, akariEntity.entity, players)
        NMSManager.nms.setTransformation(akariEntity.entity, vector, players)
        NMSManager.nms.setScale(displayEntity, displayEntity.scale, players)
        NMSManager.nms.setBillboard(displayEntity, Display.Billboard.CENTER, players)
        displayEntity.setEntityData(displayEntity, players)
    }

}
