package com.undefined.akari.algorithm.catmull

import com.undefined.akari.algorithm.Algorithm
import com.undefined.akari.camaraPath.CalculatedPath
import com.undefined.akari.camaraPath.point.CameraPoint
import org.bukkit.util.Vector

object BSplineAlgorithm: Algorithm {

    private fun bSplineInterpolate(p0: Float, p1: Float, p2: Float, p3: Float, t: Float): Float {
        val t2 = t * t
        val t3 = t2 * t

        return ((1f / 6f) * (
                (-p0 + 3 * p1 - 3 * p2 + p3) * t3 +
                        (3 * p0 - 6 * p1 + 3 * p2) * t2 +
                        (-3 * p0 + 3 * p2) * t +
                        (p0 + 4 * p1 + p2))
                )
    }

    private fun bSplineInterpolate(p0: Vector, p1: Vector, p2: Vector, p3: Vector, t: Float): Vector {
        return Vector(
            bSplineInterpolate(p0.x.toFloat(), p1.x.toFloat(), p2.x.toFloat(), p3.x.toFloat(), t),
            bSplineInterpolate(p0.y.toFloat(), p1.y.toFloat(), p2.y.toFloat(), p3.y.toFloat(), t),
            bSplineInterpolate(p0.z.toFloat(), p1.z.toFloat(), p2.z.toFloat(), p3.z.toFloat(), t)
        )
    }

    private fun bSplineInterpolate(p0: CameraPoint, p1: CameraPoint, p2: CameraPoint, p3: CameraPoint, t: Float): CameraPoint {
        return CameraPoint(
            bSplineInterpolate(p0.position, p1.position, p2.position, p3.position, t),
            bSplineInterpolate(p0.yaw, p1.yaw, p2.yaw, p3.yaw, t),
            bSplineInterpolate(p0.pitch, p1.pitch, p2.pitch, p3.pitch, t)
        )
    }

    override fun calculatePoints(pointMap: HashMap<Int, CameraPoint>, kotlinDSL: CalculatedPath.() -> Unit): CalculatedPath {
        val sortedTicks = pointMap.keys.sorted()
        val calculated: HashMap<Int, CameraPoint> = hashMapOf()

        if (pointMap.size < 4) {
            return CalculatedPath(calculated, pointMap)
        }

        val extendedTicks = mutableListOf<Int>()
        extendedTicks.add(sortedTicks.first() - 1)
        extendedTicks.addAll(sortedTicks)
        extendedTicks.add(sortedTicks.last() + 1)

        val extendedPoints = extendedTicks.map {
            pointMap[it] ?: when {
                it < sortedTicks.first() -> pointMap[sortedTicks.first()]!!
                it > sortedTicks.last() -> pointMap[sortedTicks.last()]!!
                else -> error("Unexpected missing point")
            }
        }

        for (i in 0 until extendedPoints.size - 3) {
            val p0 = extendedPoints[i]
            val p1 = extendedPoints[i + 1]
            val p2 = extendedPoints[i + 2]
            val p3 = extendedPoints[i + 3]

            val startTick = extendedTicks[i + 1]
            val endTick = extendedTicks[i + 2]
            val tickRange = endTick - startTick

            for (j in 0 until tickRange) {
                val t = j.toFloat() / tickRange
                val interpolatedPoint = bSplineInterpolate(p0, p1, p2, p3, t)
                calculated[startTick + j] = interpolatedPoint
            }
        }

        return CalculatedPath(calculated, pointMap, kotlinDSL)
    }
}