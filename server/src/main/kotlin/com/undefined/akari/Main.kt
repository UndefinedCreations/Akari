package com.undefined.akari

import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        AkariConfig.setPlugin(this)
        getCommand("fly")?.setExecutor(FlyCommand())
        getCommand("flyto")?.setExecutor(FlyToCommand())
        logger.info("Akari has been enabled!")
    }
}