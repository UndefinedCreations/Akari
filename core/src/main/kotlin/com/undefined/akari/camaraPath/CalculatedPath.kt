package com.undefined.akari.camaraPath

import com.undefined.akari.camaraPath.point.CameraPoint
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class CalculatedPath(
    val calculatedPoints: HashMap<Int, CameraPoint>,
    val originalPoints: HashMap<Int, CameraPoint>,
    kotlinDSL: CalculatedPath.() -> Unit = {}
) {

    fun clone(): CalculatedPath = CalculatedPath(calculatedPoints, originalPoints)

    fun addPosition(vector: Vector) = apply {
        calculatedPoints.forEach { it.value.addPosition(vector) }
    }

    fun addPosition(
        x: Double = 0.0,
        y: Double = 0.0,
        z: Double = 0.0
    ) = apply {
        addPosition(Vector(x, y, z))
    }

    fun addPosition(
        player: Player
    ) = apply {
        addPosition(player.location.toVector())
    }

}