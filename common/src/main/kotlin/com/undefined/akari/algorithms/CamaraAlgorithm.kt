package com.undefined.akari.algorithms

import com.undefined.akari.objects.CamaraAlgorithmType
import com.undefined.akari.objects.CamaraPoint
import java.util.SortedMap

object CamaraAlgorithm {

    fun generate(
        startTick: Int,
        startPoint: CamaraPoint,
        endPoint: CamaraPoint
    ): SortedMap<Int, CamaraPoint> = when(endPoint.camaraType) {
        CamaraAlgorithmType.SMOOTH -> SmoothAlgorithm.generate(startTick, startPoint, endPoint)
    }

}