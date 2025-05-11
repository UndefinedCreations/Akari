package com.undefined.akari

import org.bukkit.plugin.java.JavaPlugin

object AkariConfig {
    lateinit var javaPlugin: JavaPlugin
    fun setPlugin(javaPlugin: JavaPlugin) = apply { this.javaPlugin = javaPlugin }
}