@file:Suppress("SYNTHETIC_PROPERTY_WITHOUT_JAVA_ORIGIN")

package com.undefined.akari.player

import com.undefined.akari.algorithm.AlgorithmType
import com.undefined.akari.camaraPath.CalculatedPath
import com.undefined.akari.camaraPath.CameraPath
import com.undefined.akari.util.LineUtil
import org.bukkit.Location
import org.bukkit.World
import java.util.SortedMap

class CameraSequence(
    kotlinDSL: CameraSequence.() -> Unit = {}
) {

    internal val pathMap: SortedMap<Int, CalculatedPath> = sortedMapOf()
    var bridgeAlgorithm: AlgorithmType = AlgorithmType.INSTANT

    init {
        kotlinDSL(this)
    }

    /**
     * Sets smoothing algorithm for merging paths and return the modified [CameraSequence].
     */
    fun setBridgeAlgorithm(algorithmType: AlgorithmType): CameraSequence = apply {
        this.bridgeAlgorithm = algorithmType
    }

    /**
     * Add path at the end of the path list
     *
     * @param calculatedPath
     * @param time
     * @return
     */
    fun addCameraPath(calculatedPath: CalculatedPath, time: Int = 20): CameraSequence = apply {
        if (!pathMap.isEmpty() && bridgeAlgorithm != AlgorithmType.INSTANT) addBridgePath(calculatedPath, time)
        pathMap[pathMap.size] = calculatedPath
    }

    private fun addBridgePath(calculatedPath: CalculatedPath, time: Int) {
        val points = pathMap.lastEntry().value.calculatedPoints.values.toList()
        val lastPoint = points.last()
        val secondToLastPoint = points[points.size - 2]

        val firstNext = calculatedPath.calculatedPoints.values.first()
        val secondNext = calculatedPath.calculatedPoints.values.toList()[1]

        val bridgePath: CalculatedPath = CameraPath()
            .setAlgorithm(bridgeAlgorithm)
            .addCamaraPoint(secondToLastPoint, 0)
            .addCamaraPoint(lastPoint, 0)
            .addCamaraPoint(firstNext, time)
            .addCamaraPoint(secondNext, 0)
            .calculatePoints()

        //TODO Make this smoother
        pathMap[pathMap.size] = bridgePath

    }

    fun firstLocation(world: World): Location = pathMap.firstEntry().value.calculatedPoints.values.first().toLocation(world)

    fun spawnDisplayLine(world: World) {
        var past: Location? = null
        pathMap.forEach {
            val material = LineUtil.randomMaterial()
            for (point in it.value.calculatedPoints.values) {
                if (past == null) {
                    past = point.toLocation(world)
                    continue
                }
                val newLoc = point.toLocation(world)
                        LineUtil.createLine(past, newLoc, material)
                past = newLoc
            }
        }
    }

}