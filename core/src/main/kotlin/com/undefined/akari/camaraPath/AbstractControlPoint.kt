package com.undefined.akari.camaraPath

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector
import kotlin.math.acos
import kotlin.math.atan2

abstract class AbstractControlPoint<T>(
    open val position: Vector,
    open var yaw: Float,
    open var pitch: Float,
) {

    fun toLocation(world: World): Location = Location(world, position.x, position.y, position.z, yaw, pitch)

    fun setYaw(amount: Float): T = apply {
        this.yaw = amount
    } as T

    fun setPitch(amount: Float): T = apply {
        this.pitch = amount
    } as T

    fun lookAt(location: Location): T = apply {
        setYaw(180 - (Math.toDegrees(atan2(location.x, location.z))).toFloat())
        setPitch(90 - (Math.toDegrees(acos(location.y))).toFloat())
    } as T

    fun addYaw(amount: Float): T = apply {
        this.yaw += amount
    } as T

    fun addPitch(amount: Float): T = apply {
        this.pitch += amount
    } as T

    fun addPosition(vector: Vector): T = apply {
        this.position.add(vector)
    } as T

}