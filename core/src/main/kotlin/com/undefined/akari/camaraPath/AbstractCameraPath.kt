@file:Suppress("NO_REFLECTION_IN_CLASS_PATH", "UNCHECKED_CAST")

package com.undefined.akari.camaraPath

import com.undefined.akari.camaraPath.point.CameraPoint

abstract class AbstractCameraPath<T> {

    abstract val pointMap: HashMap<Int, CameraPoint>

    var localCameraPoint: CameraPoint? = null

    fun setLocalCameraPoint(cameraPoint: CameraPoint?): T {
        this.localCameraPoint = cameraPoint
        return this as T
    }

    fun useLocalCameraPoint(cameraPoint: CameraPoint?): T {
        this.localCameraPoint = cameraPoint
        return this as T
    }

    abstract fun calculatePoints(
        kotlinDSL: CalculatedPath.() -> Unit = {}
    ): CalculatedPath



}