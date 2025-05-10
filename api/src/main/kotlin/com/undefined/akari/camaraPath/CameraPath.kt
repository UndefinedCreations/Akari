package com.undefined.akari.camaraPath

import com.undefined.akari.algorithm.Algorithm
import org.bukkit.Location
import org.bukkit.World

class CameraPath(
    var world: World? = null
) {

    private val pointMap: HashMap<Int, CameraPoint> = hashMapOf()
    private var algorithm: Algorithm

    internal var calculatedPoints: HashMap<Int, CameraPoint> = hashMapOf()


    fun setAlgorithm(algorithm: Algorithm): CameraPath {
        this.algorithm = algorithm
        return this
    }

    fun addCamaraPoint(cameraPoint: CameraPoint, time: Int = 20): CameraPath {
        val highestPoint = if (pointMap.isEmpty()) 0 else pointMap.keys.maxOf { it }
        pointMap[highestPoint+time] = cameraPoint
        return this
    }

    fun addLocationPoint(location: Location, time: Int = 20): CameraPath = addCamaraPoint(location.toCamaraPoint(), time)

    fun addCamaraPath(cameraPath: CameraPath): CameraPath {

        return this
    }

}