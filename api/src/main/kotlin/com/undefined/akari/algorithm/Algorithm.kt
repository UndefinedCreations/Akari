package com.undefined.akari.algorithm

import com.undefined.akari.camaraPath.CameraPoint

interface Algorithm {

    fun calculatePoints (pointMap: HashMap<Int, CameraPoint>): HashMap<Int, CameraPoint>

}