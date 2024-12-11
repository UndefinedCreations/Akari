package com.undefined.akari.algorithms

import com.undefined.akari.objects.CamaraPoint
import com.undefined.akari.objects.CamaraRotations
import com.undefined.akari.objects.camaraPoint
import java.util.*

object SimpleAlgorithm {

    fun generate(map: SortedMap<Int, CamaraPoint>): SortedMap<Int, CamaraPoint> {

        val newMap = sortedMapOf<Int, CamaraPoint>()

        var oldPoint: Map.Entry<Int, CamaraPoint>? = null

        map.forEach {
            if (oldPoint != null) {
                if (oldPoint!!.key +1 != it.key) {
                    newMap.putAll(generatePath(oldPoint!!.key, oldPoint!!.value, it.value))
                }
            }

            for(x in 0..it.value.delay)
                newMap[it.key + x] = it.value

            oldPoint = it
        }

        newMap.putAll(map)

        println(newMap)
        return newMap
    }


    private fun generatePath(startTick: Int, oldPoint: CamaraPoint, newPoint: CamaraPoint): SortedMap<Int, CamaraPoint> {

        val xDifference = newPoint.x - oldPoint.x
        val yDifference = newPoint.y - oldPoint.y
        val zDifference = newPoint.z - oldPoint.z

        val yawDifference = (newPoint.yaw + 180) - (oldPoint.yaw + 180)
        val pitchDifference = (newPoint.pitch + 90) - (oldPoint.pitch + 90)

        val yawRotation = if (yawDifference > 0) CamaraRotations.RIGHT else CamaraRotations.LEFT
        val pitchRotations = if (pitchDifference > 0) CamaraRotations.UP else CamaraRotations.DOWN


        val xAmount = xDifference / newPoint.durationIntoPoint
        val yAmount = yDifference / newPoint.durationIntoPoint
        val zAmount = zDifference / newPoint.durationIntoPoint

        val yawAmount = yawDifference / newPoint.durationIntoPoint
        val pitchAmount = pitchDifference / newPoint.durationIntoPoint

        val map = sortedMapOf<Int, CamaraPoint>()

        for (x in 0..newPoint.durationIntoPoint) {
            map[startTick + x] = oldPoint.clone().add(xAmount * x, yAmount * x, zAmount * x).apply {
                if (yawRotation == CamaraRotations.RIGHT) yaw += yawAmount * x else yaw -= yawAmount * x
                if (pitchRotations == CamaraRotations.DOWN) pitch += pitchAmount * x else pitch -= pitchAmount * x
            }.camaraPoint()
        }

        return map
    }

}