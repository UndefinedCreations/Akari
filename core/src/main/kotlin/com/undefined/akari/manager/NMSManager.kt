package com.undefined.akari.manager

import com.undefined.akari.nms.NMS
import com.undefined.akari.exception.UnsupportedVersionException
import com.undefined.akari.nms.v1_21_5.NMS_1_21_5
import com.undefined.akari.nms.v1_21_7.NMS_1_21_7
import com.undefined.akari.nms.v1_21_8.NMS_1_21_8
import com.undefined.akari.nms.v26_1_2.NMS_26_1_2
import org.bukkit.Bukkit

object NMSManager {

    val nms: NMS by lazy {
        versions.entries.firstOrNull { version.startsWith(it.key) }?.value?.invoke()
            ?: throw UnsupportedVersionException(Bukkit.getBukkitVersion(), versions.keys)
    }
    private val version by lazy { Bukkit.getBukkitVersion() }
    private val versions: Map<String, () -> NMS> = mapOf(
        "1.21.5" to { NMS_1_21_5 },
        "1.21.7" to { NMS_1_21_7 },
        "1.21.8" to { NMS_1_21_8 },
        "26.1.2" to { NMS_26_1_2 },
    )

}