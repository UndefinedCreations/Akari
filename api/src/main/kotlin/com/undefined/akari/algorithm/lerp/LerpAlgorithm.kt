package com.undefined.akari.algorithm.lerp

import com.undefined.akari.algorithm.Algorithm
import com.undefined.akari.algorithm.lerp.LerpAlgorithm.MathUtils.lerp
import com.undefined.akari.camaraPath.CameraPoint
import org.bukkit.util.Vector

class LerpAlgorithm (): Algorithm {

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

    fun smoothPoints (a: CameraPoint, b: CameraPoint, t: Float): List<CameraPoint> {
        val points: List<CameraPoint> = listOf()
        for (i in 0 until 20) {
            points.add(CameraPoint(lerp(a,b,t)))
        }
    }


    override fun calculatePoints(pointMap: HashMap<Int, CameraPoint>): HashMap<Int, CameraPoint> {

        for (i in 0 until pointMap.keys.maxOf { it }) {
            smoothPoints()
        }

    }

    fun fuc (points: MutableList<Float> = mutableListOf(1f,4f,6f,9f), f: Float): Float {

        // Go through every point.
        // Select the first point
        // Select the next point.
        // if there are no more points, then repeat.

        //WARNING POINT AMOUNT MUST BE A PARE

        var calculateMe: MutableList<Float> = points

        while (calculateMe.size != 1) {
            val current: Float = calculateMe[0]
            val a = current
            val b = calculateMe[1]

            val nextList: MutableList<Float> = mutableListOf()

            for (i in 0 until calculateMe.size -2) { // Remove 1 from the amount
                nextList.add(MathUtils.lerp(a, b, f))
            }
            calculateMe = nextList
        }
        return calculateMe[1]
    }



}