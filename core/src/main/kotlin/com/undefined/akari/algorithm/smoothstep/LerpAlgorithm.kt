@file:Suppress("SpellCheckingInspection")

package com.undefined.akari.algorithm.smoothstep

import com.undefined.akari.algorithm.Algorithm
import com.undefined.akari.camaraPath.CalculatedPath
import com.undefined.akari.camaraPath.CameraPoint
import org.bukkit.util.Vector

object LerpAlgorithm : Algorithm {

    object MathUtils {
        fun lerp(a: Float, b: Float, f: Float): Float {
            return a + f * (b - a)
        }
        fun lerp(a: CameraPoint, b: CameraPoint, t: Float): CameraPoint {
            return CameraPoint(
                Vector(
                    lerp(a.position.x.toFloat(), b.position.x.toFloat(), t),
                    lerp(a.position.y.toFloat(), b.position.y.toFloat(), t),
                    lerp(a.position.z.toFloat(), b.position.z.toFloat(), t)
                ),
                lerp(a.yaw, b.yaw, t),
                lerp(a.pitch, b.pitch, t)
            )
        }

    }

    override fun calculatePoints(pointMap: HashMap<Int, CameraPoint>): CalculatedPath {

        val sortedTicks = pointMap.keys.sorted()
        var currentTick = sortedTicks.first()
        val calculated: HashMap<Int, CameraPoint> = hashMapOf()

        for (i in 0 until sortedTicks.size - 1) {
            val aTick = sortedTicks[i]
            val bTick = sortedTicks[i + 1]
            val a = pointMap[aTick]!!
            val b = pointMap[bTick]!!

            while (currentTick < bTick) {
                val t = (currentTick - aTick).toFloat() / (bTick - aTick)
                val interpolatedPoint = MathUtils.lerp(a, b, t)
                calculated[currentTick] = interpolatedPoint  // Add to calculated
                println("Tick: $currentTick -> $interpolatedPoint")

                currentTick++
            }
        }
        return CalculatedPath(calculated, pointMap)
    }

}