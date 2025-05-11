package com.undefined.akari.camaraPath

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector

data class CameraPoint(
    val position: Vector,
    val yaw: Float,
    val pitch: Float,
) {

    fun toLocation(world: World): Location = Location(world, position.x, position.y, position.z, yaw, pitch)

}

fun Location.toCamaraPoint(): CameraPoint = CameraPoint(this.toVector(), this.yaw, this.pitch)
