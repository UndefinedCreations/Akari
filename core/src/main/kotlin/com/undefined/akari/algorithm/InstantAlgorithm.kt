package com.undefined.akari.algorithm

import com.undefined.akari.camaraPath.CalculatedPath
import com.undefined.akari.camaraPath.point.CameraPoint

object InstantAlgorithm: Algorithm {

    override fun calculatePoints(pointMap: HashMap<Int, CameraPoint>, kotlinDSL: CalculatedPath.() -> Unit): CalculatedPath {
        return CalculatedPath(pointMap, pointMap, kotlinDSL)
    }

}