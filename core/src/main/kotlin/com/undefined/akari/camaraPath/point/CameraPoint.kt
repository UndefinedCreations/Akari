package com.undefined.akari.camaraPath.point

import org.bukkit.Location
import org.bukkit.util.Vector
import kotlin.math.atan2
import kotlin.math.sqrt

class CameraPoint(
    override var position: Vector,
    override var yaw: Float,
    override var pitch: Float,
    kotlinDSL: CameraPoint.() -> Unit = {}
): AbstractControlPoint<CameraPoint>(position, yaw, pitch) {

    constructor(
        x: Double = 0.0,
        y: Double = 0.0,
        z: Double = 0.0,
        yaw: Float = 0f,
        pitch: Float = 0f,
        kotlinDSL: CameraPoint.() -> Unit = {}
    ): this(
        Vector(x, y, z), yaw, pitch, kotlinDSL
    )

    init {
        kotlinDSL(this)
    }

}

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