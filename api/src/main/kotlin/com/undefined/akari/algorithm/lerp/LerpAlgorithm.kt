package com.undefined.akari.algorithm.lerp

import com.undefined.akari.algorithm.Algorithm
import com.undefined.akari.camaraPath.CameraPoint
import org.bukkit.Location

abstract class LerpAlgorithm (val shapingFunction: ShapingFunction): Algorithm {

    object MathUtils {
        fun lerp(a: Float, b: Float, f: Float): Float {
            return a + f * (b - a)
        }

    }

    fun calcAtTick (tick: Int, points: List<CameraPoint>) {

    }

    override fun calculatePoints(pointMap: HashMap<Int, CameraPoint>): HashMap<Int, CameraPoint> {

        val points: List<CameraPoint>

        for (i in 0 until points.size - 1) {
            val p: CameraPoint = points[i]
            val p2: CameraPoint = points[i+1]

            val
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