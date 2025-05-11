package com.undefined.akari.algorithm.catmull

import com.undefined.akari.algorithm.Algorithm
import com.undefined.akari.camaraPath.CalculatedPath
import com.undefined.akari.camaraPath.CameraPoint
import org.bukkit.util.Vector

object CatmullRomAlgorithm: Algorithm {

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
    fun catmullInterpolate(p0: CameraPoint, p1: CameraPoint, p2: CameraPoint, p3: CameraPoint, t: Float): CameraPoint {
        return CameraPoint(
            Vector(
                catmullInterpolate(p0.position.x.toFloat(), p1.position.x.toFloat(), p2.position.x.toFloat(), p3.position.x.toFloat(), t),
                catmullInterpolate(p0.position.y.toFloat(), p1.position.y.toFloat(), p2.position.y.toFloat(), p3.position.y.toFloat(), t),
                catmullInterpolate(p0.position.z.toFloat(), p1.position.z.toFloat(), p2.position.z.toFloat(), p3.position.z.toFloat(), t)
            ),
            catmullInterpolate(p0.yaw, p1.yaw, p2.yaw, p3.yaw, t),
            catmullInterpolate(p0.pitch, p1.pitch, p2.pitch, p3.pitch, t)
        )
    }

    override fun calculatePoints(pointMap: HashMap<Int, CameraPoint>): CalculatedPath {

        val sortedTicks = pointMap.keys.sorted()
        val calculated: HashMap<Int, CameraPoint> = hashMapOf()

        if (pointMap.size < 4) {
            return CalculatedPath(calculated, pointMap)
        }

        val extendedTicks = mutableListOf<Int>()
        extendedTicks.add(sortedTicks.first() - 1) // pad for p0
        extendedTicks.addAll(sortedTicks)
        extendedTicks.add(sortedTicks.last() + 1)  // pad for p3

        // Fill padding with closest real values
        val extendedPoints = extendedTicks.map {
            pointMap[it] ?: when {
                it < sortedTicks.first() -> pointMap[sortedTicks.first()]!!
                it > sortedTicks.last() -> pointMap[sortedTicks.last()]!!
                else -> error("Unexpected missing point")
            }
        }

        for (i in 1 until extendedPoints.size - 2) {
            val p0 = extendedPoints[i - 1]
            val p1 = extendedPoints[i]
            val p2 = extendedPoints[i + 1]
            val p3 = extendedPoints[i + 2]

            val startTick = extendedTicks[i]
            val endTick = extendedTicks[i + 1]
            val tickRange = endTick - startTick

            for (j in 0 until tickRange) {
                val t = j.toFloat() / tickRange
                val interpolatedPoint: CameraPoint = catmullInterpolate(p0, p1, p2, p3, t)
                calculated[startTick + j] = interpolatedPoint
            }
        }

        return CalculatedPath(calculated, pointMap)
    }


}