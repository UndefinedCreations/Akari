package com.undefined.akari.algorithm

import com.undefined.akari.camaraPath.CalculatedPath
import com.undefined.akari.camaraPath.point.CameraPoint

interface Algorithm {
    fun calculatePoints (pointMap: HashMap<Int, CameraPoint>, kotlinDSL: CalculatedPath.() -> Unit): CalculatedPath
}

// Put this in a shared place, e.g. a new AngleUtil.kt
object AngleUtil {
    fun unwrapAngle(from: Float, to: Float): Float {
        var delta = to - from
        // Normalize delta to [-180, 180]
        delta = ((delta % 360f) + 540f) % 360f - 180f
        return from + delta
    }

    fun unwrapPoints(points: List<CameraPoint>): List<CameraPoint> {
        if (points.isEmpty()) return points
        val result = mutableListOf(points[0])
        for (i in 1 until points.size) {
            val prev = result[i - 1]
            val curr = points[i]
            result.add(CameraPoint(
                curr.position.clone(),
                unwrapAngle(prev.yaw, curr.yaw),
                unwrapAngle(prev.pitch, curr.pitch)
            ))
        }
        return result
    }
}