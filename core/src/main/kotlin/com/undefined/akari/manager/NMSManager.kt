package com.undefined.akari.manager

import com.undefined.akari.nms.NMS
import com.undefined.akari.exception.UnsupportedVersionException
import com.undefined.akari.nms.v1_21_5.NMS_1_21_5
import com.undefined.akari.nms.v1_21_7.NMS_1_21_7
import org.bukkit.Bukkit

object NMSManager {

    val nms: NMS by lazy { versions[version]?.let { it() } ?: throw UnsupportedVersionException(versions.keys) }
    private val version by lazy { Bukkit.getBukkitVersion().split("-")[0] }
    private val versions: Map<String, () -> NMS> = mapOf(
        "1.21.5" to { NMS_1_21_5 },
        "1.21.7" to { NMS_1_21_7 },
    )

}