package com.undefined.akari.camaraPath

data class CalculatedPath(
    val calculatedPoints: HashMap<Int, CameraPoint>,
    val originalPoints: HashMap<Int, CameraPoint>,
)