package com.undefined.akari.objects

import org.bukkit.Location
import org.bukkit.World

class CamaraPoint(
    world: World,
    x: Double,
    y: Double,
    z: Double,
    yaw: Float = 0f,
    pitch: Float = 0f
): Location(world, x, y, z, yaw, pitch)

fun Location.camaraPoint() = CamaraPoint(world!!, x, y, z, yaw, pitch)