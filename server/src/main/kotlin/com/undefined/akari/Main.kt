package com.undefined.akari

import com.undefined.akari.manager.DisplayManager
import com.undefined.akari.objects.camaraPoint
import com.undefined.stellar.StellarCommand
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main: JavaPlugin() {

    override fun onEnable() {

        StellarCommand("testing")
            .addExecution<Player> {

                CamaraSequence(this@Main)
                    .addPoint(sender.location.clone().add(0.0, 10.0, 5.0).camaraPoint())
                    .play(sender)


            }.register(this)

    }

    override fun onDisable() {

    }

}