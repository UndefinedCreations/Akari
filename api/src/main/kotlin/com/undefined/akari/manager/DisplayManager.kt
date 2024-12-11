package com.undefined.akari.manager

import com.undefined.akari.entity.CamaraDisplay
import com.undefined.akari.exception.DifferentWorldException
import com.undefined.akari.util.NMSVersion


object DisplayManager {

    fun create(): CamaraDisplay = when(NMSVersion.version) {
        "1.21.3" -> com.undefined.akari.v1_21_3.CamaraDisplay()
        else -> throw UnsupportedOperationException()
    }

}