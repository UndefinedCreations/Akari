package com.undefined.akari

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        AkariConfig.setPlugin(this)


    }
}