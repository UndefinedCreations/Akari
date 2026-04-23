@file:Suppress("SpellCheckingInspection")

package com.undefined.akari.algorithm.smoothstep

import com.undefined.akari.algorithm.Algorithm
import com.undefined.akari.algorithm.AngleUtil
import com.undefined.akari.algorithm.smoothstep.SmoothStepAlgorithm.MathUtils.unwrapAngle
import com.undefined.akari.camaraPath.CalculatedPath
import com.undefined.akari.camaraPath.point.CameraPoint
import org.bukkit.util.Vector

object SmoothStepAlgorithm : Algorithm {

    object MathUtils {
        fun lerp(a: Float, b: Float, f: Float): Float {
            return a + f * (b - a)
        }

        fun unwrapAngle(from: Float, to: Float): Float {
            var delta = (to - from) % 360f
            if (delta > 180f) delta -= 360f
            if (delta < -180f) delta += 360f
            return from + delta
        }

        fun smoothStep(a: Float, b: Float, f: Float): Float {
            val t = f.coerceIn(0f, 1f)
            val smooth = t * t * (3 - 2 * t)
            return a + smooth * (b - a)
        }

        fun wrapDelta(from: Float, to: Float): Float {
            var delta = (to - from) % 360f
            if (delta > 180f) delta -= 360f
            if (delta < -180f) delta += 360f
            return from + delta
        }

        fun smoothStep(a: CameraPoint, b: CameraPoint, t: Float): CameraPoint {
            val adjustedYaw = wrapDelta(a.yaw, b.yaw)
            val adjustedPitch = wrapDelta(a.pitch, b.pitch)
            return CameraPoint(
                Vector(
                    smoothStep(a.position.x.toFloat(), b.position.x.toFloat(), t),
                    smoothStep(a.position.y.toFloat(), b.position.y.toFloat(), t),
                    smoothStep(a.position.z.toFloat(), b.position.z.toFloat(), t)
                ),
                smoothStep(a.yaw, adjustedYaw, t),
                smoothStep(a.pitch, adjustedPitch, t)
            )
        }

        fun lerp(a: CameraPoint, b: CameraPoint, t: Float): CameraPoint {
            val adjustedYaw = wrapDelta(a.yaw, b.yaw)
            val adjustedPitch = wrapDelta(a.pitch, b.pitch)
            return CameraPoint(
                Vector(
                    lerp(a.position.x.toFloat(), b.position.x.toFloat(), t),
                    lerp(a.position.y.toFloat(), b.position.y.toFloat(), t),
                    lerp(a.position.z.toFloat(), b.position.z.toFloat(), t)
                ),
                lerp(a.yaw, adjustedYaw, t),
                lerp(a.pitch, adjustedPitch, t)
            )
        }
    }

    override fun calculatePoints(pointMap: HashMap<Int, CameraPoint>, kotlinDSL: CalculatedPath.() -> Unit): CalculatedPath {
        val sortedTicks = pointMap.keys.sorted()
        var currentTick = sortedTicks.first()
        val calculated: HashMap<Int, CameraPoint> = hashMapOf()

        val rawPoints = sortedTicks.map { pointMap[it]!! }
        val unwrapped = AngleUtil.unwrapPoints(rawPoints)

        for (i in 0 until sortedTicks.size - 1) {
            val aTick = sortedTicks[i]
            val bTick = sortedTicks[i + 1]
            val a = unwrapped[i]
            val b = unwrapped[i + 1]

            while (currentTick < bTick) {
                val t = (currentTick - aTick).toFloat() / (bTick - aTick)
                calculated[currentTick] = CameraPoint(
                    Vector(
                        MathUtils.smoothStep(a.position.x.toFloat(), b.position.x.toFloat(), t),
                        MathUtils.smoothStep(a.position.y.toFloat(), b.position.y.toFloat(), t),
                        MathUtils.smoothStep(a.position.z.toFloat(), b.position.z.toFloat(), t)
                    ),
                    MathUtils.smoothStep(a.yaw, b.yaw, t),
                    MathUtils.smoothStep(a.pitch, b.pitch, t)
                )
                currentTick++
            }
        }
        return CalculatedPath(calculated, pointMap, kotlinDSL)
    }
}