@file:Suppress("NO_REFLECTION_IN_CLASS_PATH")

package com.undefined.akari.camaraPath

import com.undefined.akari.algorithm.Algorithm
import com.undefined.akari.algorithm.AlgorithmType
import org.bukkit.Location

class CameraPath (): AbstractCameraPath() {

    override val pointMap: HashMap<Int, CameraPoint> = hashMapOf()
    var algorithm: Algorithm = AlgorithmType.SMOOTHSTEP.klass

    fun setAlgorithm(algorithmType: AlgorithmType): CameraPath {
        this.algorithm = algorithmType.klass
        return this
    }

    fun addCamaraPoint(cameraPoint: CameraPoint, time: Int = 20): CameraPath {
        val highestPoint = if (pointMap.isEmpty()) 0 else pointMap.keys.maxOf { it }
        pointMap[highestPoint+time] = cameraPoint
        return this
    }

    override fun calculatePoints(): CalculatedPath = algorithm.calculatePoints(pointMap)

}