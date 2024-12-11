package com.undefined.akari.algorithms

import com.undefined.akari.objects.CamaraPoint
import com.undefined.akari.objects.camaraPoint
import org.bukkit.Location
import java.util.SortedMap

object SmoothAlgorithm {

    fun generate(map: SortedMap<Int, CamaraPoint>): SortedMap<Int, CamaraPoint> {
        val sortedPoints = map.toSortedMap()
        val resultPoints = sortedMapOf<Int, CamaraPoint>()

        val keys = sortedPoints.keys.toList()
        val values = sortedPoints.values.toList()

        if (keys.size < 2) throw IllegalArgumentException("At least 2 points are required for interpolation.")

        val totalTicks = keys.last()

        for (tick in 0..totalTicks) {
            val t = tick.toDouble() / totalTicks
            val point = interpolatePoints(values, t)
            resultPoints[tick] = point
        }

        return resultPoints
    }

    private fun interpolatePoints(points: List<CamaraPoint>, t: Double): CamaraPoint {
        // Directly return start or end point when t is 0 or 1
        if (t <= 0.0) return points[0]
        if (t >= 1.0) return points[1]

        val n = points.size
        val p1 = ((t * (n - 1)).toInt()).coerceAtMost(n - 2)
        val p0 = (p1 - 1).coerceAtLeast(0)
        val p2 = (p1 + 1).coerceAtMost(n - 1)
        val p3 = (p2 + 1).coerceAtMost(n - 1)

        val localT = (t * (n - 1)) - p1
        val interpolatedPosition = catmullRomPosition(points[p0], points[p1], points[p2], points[p3], localT)
        val interpolatedYaw = interpolateAngle(points[p1].yaw, points[p2].yaw, localT).toFloat()
        val interpolatedPitch = interpolateAngle(points[p1].pitch, points[p2].pitch, localT).toFloat()

        return interpolatedPosition.camaraPoint().apply {
            yaw = interpolatedYaw
            pitch = interpolatedPitch
        }
    }

    private fun catmullRomPosition(p0: Location, p1: Location, p2: Location, p3: Location, t: Double): Location {
        return Location(p0.world,
            lerpSmooth(p0.x, p1.x, p2.x, p3.x, t),
            lerpSmooth(p0.y, p1.y, p2.y, p3.y, t),
            lerpSmooth(p0.z, p1.z, p2.z, p3.z, t)
        )
    }

    private fun lerpSmooth(d0: Double, d1: Double, d2: Double, d3: Double, t: Double): Double {
        val t2 = t * t
        val t3 = t2 * t
        return 0.5 * (2 * d1 + (-d0 + d2) * t + (2 * d0 - 5 * d1 + 4 * d2 - d3) * t2 + (-d0 + 3 * d1 - 3 * d2 + d3) * t3)
    }

    private fun interpolateAngle(angle1: Float, angle2: Float, t: Double): Double {
        val delta = ((angle2 - angle1 + 360) % 360)
        val shortestDelta = if (delta > 180) delta - 360 else delta
        return (angle1 + shortestDelta * t)
    }

}