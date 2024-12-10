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

                val c = DisplayManager.create()

                c.spawn(source.eyeLocation.camaraPoint(), source)

                source.gameMode = GameMode.SPECTATOR
                c.interpolationDuration(20)
                c.setViewer(source)

                c.teleport(source.eyeLocation.add(0.0, 5.0, 0.0).camaraPoint())

            }.register(this)

    }

    override fun onDisable() {

    }

}