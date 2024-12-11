package com.undefined.akari.objects

import com.undefined.akari.exception.NegativeNumberException
import org.bukkit.Location
import org.bukkit.World

class CamaraPoint(
    world: World,
    x: Double,
    y: Double,
    z: Double,
    yaw: Float = 0f,
    pitch: Float = 0f,
    val delay: Int = 0,
    val durationIntoPoint: Int = 40,
    val camaraType: CamaraAlgorithmType = CamaraAlgorithmType.SMOOTH
): Location(world, x, y, z, yaw, pitch) {

    init {

        if (delay < 0 || durationIntoPoint < 0) throw NegativeNumberException(if (delay < 0) delay else durationIntoPoint)

    }

}

fun Location.camaraPoint() = CamaraPoint(world!!, x, y, z, yaw, pitch)