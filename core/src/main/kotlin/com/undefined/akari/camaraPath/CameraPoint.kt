package com.undefined.akari.camaraPath

import org.bukkit.Location
import org.bukkit.util.Vector

class CameraPoint(
    override val position: Vector,
    override var yaw: Float,
    override var pitch: Float,
): AbstractControlPoint<CameraPoint>(position, yaw, pitch)

fun Location.toCamaraPoint(): CameraPoint = CameraPoint(this.toVector(), this.yaw, this.pitch)
