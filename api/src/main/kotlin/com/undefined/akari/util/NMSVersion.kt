package com.undefined.akari.util

import org.bukkit.Bukkit

object NMSVersion {
    val version by lazy { Bukkit.getBukkitVersion().split("-")[0] }
}