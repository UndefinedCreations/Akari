@file:Suppress("NO_REFLECTION_IN_CLASS_PATH")

package com.undefined.akari.camaraPath

import com.undefined.akari.algorithm.Algorithm
import com.undefined.akari.algorithm.AlgorithmType
import com.undefined.akari.camaraPath.point.CameraPoint

class CameraPath(
    kotlinDSL: CameraPath.() -> Unit = {}
): AbstractCameraPath<CameraPath>() {

    init {
        kotlinDSL(this)
    }
    var algorithm: Algorithm = AlgorithmType.SMOOTHSTEP.klass

    fun setAlgorithm(algorithmType: AlgorithmType) = apply {
        this.algorithm = algorithmType.klass
    }

    fun addCamaraPoint(
        cameraPoint: CameraPoint,
        time: Int = 20,
        kotlinDSL: CameraPoint.() -> Unit = {}
    ) = apply {
        val highestPoint = if (pointMap.isEmpty()) 0 else pointMap.keys.maxOf { it }
        pointMap[highestPoint+time] = if (localCameraPoint == null) cameraPoint else cameraPoint
            .addPosition(localCameraPoint!!.position)
            .setYaw(localCameraPoint!!.yaw)
            .setPitch(localCameraPoint!!.pitch)
    }

    fun addCamaraPoint(
        x: Double = 0.0,
        y: Double = 0.0,
        z: Double = 0.0,
        yaw: Float = 0f,
        pitch: Float = 0f,
        time: Int = 20,
        kotlinDSL: CameraPoint.() -> Unit = {}
    ) = apply {
        addCamaraPoint(CameraPoint(x, y, z, yaw, pitch, kotlinDSL), time)
    }

    override fun calculatePoints(
        kotlinDSL: CalculatedPath.() -> Unit
    ): CalculatedPath = algorithm.calculatePoints(pointMap, kotlinDSL)

}