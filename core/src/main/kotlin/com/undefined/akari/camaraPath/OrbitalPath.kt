package com.undefined.akari.camaraPath

import org.bukkit.EntityEffect
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

class OrbitalPath (
    private var center: Location, var radius: Float = 5.0f, var height: Double, var time: Int = 60
): AbstractCameraPath() {

    override val pointMap: HashMap<Int, CameraPoint> = hashMapOf()

    fun setCenter(center: Location) = apply {
        this.center = center
    }

    override fun calculatePoints(): CalculatedPath {
        val calculated: HashMap<Int, CameraPoint> = hashMapOf()

        val angleStep = 360.0 / time

        for (tick in 0 until time) {
            val angleRadians = Math.toRadians(tick * angleStep)

            val x = center.x + radius * cos(angleRadians)
            val z = center.z + radius * sin(angleRadians)
            val y = center.y + height

            val position = Vector(x, y, z)

            val loc = Location(center.world, position.x, position.y, position.z, 0.0f, 0.0f)
            calculated[tick] = loc.lookAt(center).toCameraPoint()
        }


        return CalculatedPath(calculated, hashMapOf(0 to center.toCameraPoint()))
    }

}