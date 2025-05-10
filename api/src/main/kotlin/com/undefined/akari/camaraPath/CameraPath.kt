package com.undefined.akari.camaraPath

import com.undefined.akari.algorithm.lerp.LerpAlgorithm
import com.undefined.akari.algorithm.lerp.LerpAlgorithm.MathUtils.lerp
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector

class CameraPath(
    var world: World? = null
) {

    private val pointMap: HashMap<Int, CameraPoint> = hashMapOf()

//    private val nextPath: CameraPath

    internal var calculatedPoints: HashMap<Int, CameraPoint> = hashMapOf()

    fun addCamaraPoint(cameraPoint: CameraPoint, time: Int = 20): CameraPath {
        val highestPoint = if (pointMap.isEmpty()) 0 else pointMap.keys.maxOf { it }
        pointMap[highestPoint+time] = cameraPoint
        return this
    }

    fun addLocationPoint(location: Location, time: Int = 20): CameraPath = addCamaraPoint(location.toCamaraPoint(), time)

    fun addCamaraPath(cameraPath: CameraPath): CameraPath {

        return this
    }



    fun smoothStep(a: CameraPoint, b: CameraPoint, t: Float): CameraPoint {
        return CameraPoint(
            Vector(
                LerpAlgorithm.MathUtils.lerp(a.position.x.toFloat(), b.position.x.toFloat(), t),
                LerpAlgorithm.MathUtils.lerp(a.position.y.toFloat(), b.position.y.toFloat(), t),
                LerpAlgorithm.MathUtils.lerp(a.position.z.toFloat(), b.position.z.toFloat(), t)
            ),
            LerpAlgorithm.MathUtils.lerp(a.yaw, b.yaw, t),
            LerpAlgorithm.MathUtils.lerp(a.pitch, b.pitch, t)
        )
    }
}