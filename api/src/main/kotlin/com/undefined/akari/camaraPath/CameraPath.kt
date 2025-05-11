@file:Suppress("NO_REFLECTION_IN_CLASS_PATH")

package com.undefined.akari.camaraPath

import com.undefined.akari.algorithm.Algorithm
import com.undefined.akari.algorithm.AlgorithmType
import com.undefined.akari.algorithm.lerp.LerpAlgorithm
import org.bukkit.Location
import org.bukkit.World
import kotlin.reflect.KClass

class CameraPath {

    private val pointMap: HashMap<Int, CameraPoint> = hashMapOf()
    private var algorithm: Algorithm = LerpAlgorithm()

    fun setAlgorithm(algorithmType: AlgorithmType): CameraPath {
        this.algorithm = algorithmType.klass.constructors.first().call()
        return this
    }

    fun addCamaraPoint(cameraPoint: CameraPoint, time: Int = 20): CameraPath {
        val highestPoint = if (pointMap.isEmpty()) 0 else pointMap.keys.maxOf { it }
        pointMap[highestPoint+time] = cameraPoint
        return this
    }

    fun calculatePoints(): CalculatedPath = CalculatedPath(algorithm.calculatePoints(pointMap))

    fun addLocationPoint(location: Location, time: Int = 20): CameraPath = addCamaraPoint(location.toCamaraPoint(), time)

}