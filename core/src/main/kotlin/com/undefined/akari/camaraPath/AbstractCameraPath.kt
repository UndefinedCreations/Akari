@file:Suppress("NO_REFLECTION_IN_CLASS_PATH")

package com.undefined.akari.camaraPath

import com.undefined.akari.algorithm.Algorithm
import com.undefined.akari.algorithm.AlgorithmType
import org.bukkit.Location

abstract class AbstractCameraPath {

    abstract val pointMap: HashMap<Int, CameraPoint>

    abstract fun calculatePoints(): CalculatedPath



}