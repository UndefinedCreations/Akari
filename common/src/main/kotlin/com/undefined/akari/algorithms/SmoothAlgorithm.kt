package com.undefined.akari.algorithms

import com.undefined.akari.objects.CamaraPoint
import com.undefined.akari.objects.camaraPoint
import java.util.SortedMap

object SmoothAlgorithm {

    fun generate(startTick: Int, startPoint: CamaraPoint, endPoint: CamaraPoint): SortedMap<Int, CamaraPoint> {

        val xDifference = endPoint.x - startPoint.x
        val yDifference = endPoint.y - startPoint.y
        val zDifference = endPoint.z - startPoint.z

        val yawDifference = endPoint.yaw - startPoint.yaw
        val pitchDifference = endPoint.pitch - startPoint.pitch

        val xAmount = xDifference / endPoint.durationIntoPoint
        val yAmount = yDifference / endPoint.durationIntoPoint
        val zAmount = zDifference / endPoint.durationIntoPoint

        val yawAmount = yawDifference / endPoint.durationIntoPoint
        val pitchAmount = pitchDifference / endPoint.durationIntoPoint

        val map: SortedMap<Int, CamaraPoint> = sortedMapOf()

        for (x in 0..endPoint.durationIntoPoint) {
            map[startTick + x] = startPoint.clone().add(xAmount * x, yAmount * x, zAmount * x).apply {
                this.yaw += yawAmount*x
                this.pitch += pitchAmount*x
            }.camaraPoint()
        }
        return map
    }

}