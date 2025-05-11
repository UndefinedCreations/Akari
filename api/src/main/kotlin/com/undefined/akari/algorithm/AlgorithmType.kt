package com.undefined.akari.algorithm

import com.undefined.akari.algorithm.catmull.CatmullRomAlgorithm
import com.undefined.akari.algorithm.smoothstep.SmoothStepAlgorithm

enum class AlgorithmType(val klass: Algorithm) {
    SMOOTHSTEP(SmoothStepAlgorithm),
    CATMULLROM(CatmullRomAlgorithm),
}