package com.undefined.akari.camaraPath

import org.bukkit.Location
import org.bukkit.util.Vector
import kotlin.math.atan2
import kotlin.math.sqrt

class CameraPoint(
    override var position: Vector,
    override var yaw: Float,
    override var pitch: Float,
): AbstractControlPoint<CameraPoint>(position, yaw, pitch)

fun Location.toCameraPoint(): CameraPoint = CameraPoint(this.toVector(), this.yaw, this.pitch)
fun Location.lookAt(target: Location): Location {
    val dx = target.x - this.x
    val dy = target.y - this.y
    val dz = target.z - this.z

    val distanceXZ = sqrt(dx * dx + dz * dz)

    val yaw = Math.toDegrees(atan2(-dx, dz)).toFloat()
    val pitch = Math.toDegrees(-atan2(dy, distanceXZ)).toFloat()

    val newLoc = this.clone()
    newLoc.yaw = yaw
    newLoc.pitch = pitch
    return newLoc
}