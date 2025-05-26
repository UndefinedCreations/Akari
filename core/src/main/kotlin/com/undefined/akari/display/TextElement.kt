package com.undefined.akari.display

import com.undefined.akari.manager.NMSManager
import com.undefined.akari.player.AkariEntity
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class TextElement(
    override val startTick: Int,
    override val aliveTime: Int,
    var text: String,
    override var offset: Double = 5.0,
    override val scale: Vector = Vector(1,1,1),
    override val kotlinDSL: TextElement.() -> Unit = {}
) : DisplayElement<TextElement>(
    startTick, aliveTime, offset, scale, kotlinDSL
) {

    init {
        kotlinDSL(this)
    }

    val textChangeMap: HashMap<Int, String> = hashMapOf()

    fun setText(
        tick: Int,
        string: String
    ) = apply {
        textChangeMap[tick] = string
    }


    override fun createAkariEntity(world: World): AkariEntity {
        val textDisplay = NMSManager.nms.createTextDisplay(world)
        val serverEntity = NMSManager.nms.createServerEntity(textDisplay, world)
        return AkariEntity(textDisplay, serverEntity)
    }

    override fun setEntityData(entity: Any, player: List<Player>) {
        NMSManager.nms.setTextTextDisplay(entity, text, player)
    }
}