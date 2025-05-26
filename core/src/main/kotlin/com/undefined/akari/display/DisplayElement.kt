package com.undefined.akari.display

import com.undefined.akari.manager.NMSManager
import com.undefined.akari.player.AkariEntity
import org.bukkit.World
import org.bukkit.entity.Display
import org.bukkit.entity.Player
import org.bukkit.util.Vector

@Suppress("UNCHECKED_CAST")
abstract class DisplayElement<T: DisplayElement<T>>(
    open val startTick: Int,
    open val aliveTime: Int,
    open var offset: Double = 5.0,
    open val scale: Vector = Vector(1,1,1),
    open val kotlinDSL: T.() -> Unit = {}
) {

    val movementList: MutableList<DisplayMovement> = mutableListOf()

    /**
     *
     * @param the time it takes to move to the new location
     */
    fun moveTo(
        startTick: Int,
        x: Double,
        y: Double,
        z: Double = offset,
        time: Int = 20
    ): T {
        movementList.add(DisplayMovement(startTick, x, y, z, time))
        return this as T
    }



    abstract fun createAkariEntity(world: World): AkariEntity

    abstract fun setEntityData(entity: Any, player: List<Player>)

}