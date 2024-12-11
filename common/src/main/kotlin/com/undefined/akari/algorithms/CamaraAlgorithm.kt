package com.undefined.akari.algorithms

import com.undefined.akari.objects.CamaraAlgorithmType
import com.undefined.akari.objects.CamaraPoint
import java.util.SortedMap

object CamaraAlgorithm {

    fun generate(
        camaraAlgorithmType: CamaraAlgorithmType,
        map: SortedMap<Int, CamaraPoint>
    ): SortedMap<Int, CamaraPoint> = when(camaraAlgorithmType) {
        CamaraAlgorithmType.SMOOTH -> SmoothAlgorithm.generate(map)
        CamaraAlgorithmType.SIMPLE -> SimpleAlgorithm.generate(map)
    }

}