package com.undefined.akari.algorithm

import com.undefined.akari.camaraPath.CalculatedPath
import com.undefined.akari.camaraPath.CameraPoint

object InstantAlgorithm: Algorithm {

    override fun calculatePoints(pointMap: HashMap<Int, CameraPoint>): CalculatedPath {
        return CalculatedPath(pointMap, pointMap)
    }

}