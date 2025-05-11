package com.undefined.akari.algorithm.catmull

import com.undefined.akari.algorithm.Algorithm
import com.undefined.akari.camaraPath.CalculatedPath
import com.undefined.akari.camaraPath.CameraPoint
import org.bukkit.util.Vector

class CatmullRomAlgorithm: Algorithm {

    fun catmullInterpolate(p0: Float, p1: Float, p2: Float, p3: Float, t: Float): Float {
        val t2 = t * t
        val t3 = t2 * t
        return 0.5f * ((2 * p1) +
                (-p0 + p2) * t +
                (2*p0 - 5*p1 + 4*p2 - p3) * t2 +
                (-p0 + 3*p1 - 3*p2 + p3) * t3)
    }
    fun catmullInterpolate(p0: Vector, p1: Vector, p2: Vector, p3: Vector, t: Float): Vector {
        return Vector(
            catmullInterpolate(p0.x.toFloat(), p1.x.toFloat(), p2.x.toFloat(), p3.x.toFloat(), t),
            catmullInterpolate(p0.y.toFloat(), p1.y.toFloat(), p2.y.toFloat(), p3.y.toFloat(), t),
            catmullInterpolate(p0.z.toFloat(), p1.z.toFloat(), p2.z.toFloat(), p3.z.toFloat(), t)
        )
    }

    override fun calculatePoints(pointMap: HashMap<Int, CameraPoint>): CalculatedPath {

        val calculated: HashMap<Int, CameraPoint> = hashMapOf()
        var currentTick: Int = 0


    }


}