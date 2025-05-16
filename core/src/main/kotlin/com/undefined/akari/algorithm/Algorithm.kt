package com.undefined.akari.algorithm

import com.undefined.akari.camaraPath.CalculatedPath
import com.undefined.akari.camaraPath.point.CameraPoint

interface Algorithm {
    fun calculatePoints (pointMap: HashMap<Int, CameraPoint>, kotlinDSL: CalculatedPath.() -> Unit): CalculatedPath
}