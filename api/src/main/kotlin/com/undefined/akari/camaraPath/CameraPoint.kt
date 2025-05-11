package com.undefined.akari.camaraPath

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector

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
