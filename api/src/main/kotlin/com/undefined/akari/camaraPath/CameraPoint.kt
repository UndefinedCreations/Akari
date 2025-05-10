package com.undefined.akari.camaraPath

import org.bukkit.Location
import org.bukkit.util.Vector

data class CameraPoint(
    val position: Vector,
    val yaw: Float,
    val pitch: Float,
)

fun Location.toCamaraPoint(): CameraPoint = CameraPoint(this.toVector(), this.yaw, this.pitch)
