package com.undefined.akari.camaraPath.point

import org.bukkit.util.Vector

open class ControlPoint (
    override var position: Vector,
    override var yaw: Float,
    override var pitch: Float,
    kotlinDSL: ControlPoint.() -> Unit = {}
): AbstractControlPoint<ControlPoint>(position, yaw, pitch) {

    init {
        kotlinDSL(this)
    }

}