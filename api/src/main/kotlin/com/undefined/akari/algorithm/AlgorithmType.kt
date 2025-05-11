package com.undefined.akari.algorithm

import com.undefined.akari.algorithm.lerp.LerpAlgorithm
import kotlin.reflect.KClass

enum class AlgorithmType(val klass: KClass<out Algorithm>) {
    LERP(LerpAlgorithm::class)
}