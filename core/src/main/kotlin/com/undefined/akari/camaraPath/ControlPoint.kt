package com.undefined.akari.camaraPath

import org.bukkit.util.Vector

open class ControlPoint (
    override var position: Vector,
    override var yaw: Float,
    override var pitch: Float,
): AbstractControlPoint<ControlPoint>(position, yaw, pitch)