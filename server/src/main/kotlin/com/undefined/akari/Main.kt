package com.undefined.akari

import com.undefined.lynx.LynxConfig
import com.undefined.stellar.StellarCommand
import com.undefined.stellar.StellarConfig
import com.undefined.stellar.argument.list.OnlinePlayersArgument
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        AkariConfig.setPlugin(this)
        LynxConfig.setPlugin(this)
        StellarConfig.setPlugin(this)

        TestCommand.register()
    }
}