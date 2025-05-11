package com.undefined.akari.camaraPath

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector
import kotlin.math.acos
import kotlin.math.atan2

data class CameraPoint(
    val position: Vector,
    var yaw: Float,
    var pitch: Float,
) {

    fun toLocation(world: World): Location = Location(world, position.x, position.y, position.z, yaw, pitch)

    fun setYaw(amount: Float) = apply {
        this.yaw = amount
    }
    fun setPitch(amount: Float) = apply {
        this.pitch = amount
    }
    fun lookAt(location: Location) = apply {
        setYaw(180 - (Math.toDegrees(atan2(location.x, location.z))).toFloat())
        setPitch(90 - (Math.toDegrees(acos(location.y))).toFloat())
    }
    fun addYaw(amount: Float) = apply {
        this.yaw += amount
    }
    fun addPitch(amount: Float) = apply {
        this.pitch += amount
    }
    fun addPosition(vector: Vector) = apply {
        this.position.add(vector)
    }

}

fun Location.toCamaraPoint(): CameraPoint = CameraPoint(this.toVector(), this.yaw, this.pitch)
