package com.undefined.akari.camaraPath.point

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.util.Vector
import kotlin.math.acos
import kotlin.math.atan2

@Suppress("UNCHECKED_CAST")
abstract class AbstractControlPoint<T>(
    open var position: Vector,
    open var yaw: Float,
    open var pitch: Float
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

    fun lookAt(entity: Entity): T = apply {
        lookAt(entity.location)
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
    fun setPosition(vector: Vector): T = apply {
        this.position = vector
    } as T
    fun setPosition(x: Double,y: Double,z: Double): T = apply {
        this.position = Vector(x,y,z)
    } as T
    fun setPosition(x: Int,y: Int,z: Int,): T = apply {
        this.position = Vector(x,y,z)
    } as T
    fun setPosition(x: Float,y: Float,z: Float,): T = apply {
        this.position = Vector(x,y,z)
    } as T

}