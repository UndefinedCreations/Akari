package com.undefined.akari

import com.undefined.akari.manager.DisplayManager
import com.undefined.akari.objects.CamaraAlgorithmType
import com.undefined.akari.objects.camaraPoint
import com.undefined.stellar.StellarCommand
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import kotlin.random.Random

class Main: JavaPlugin() {

    override fun onEnable() {

        StellarCommand("testing")
            .addExecution<Player> {

                CamaraSequence(this@Main, CamaraAlgorithmType.SMOOTH)
                    .addPoint(source.eyeLocation.clone().add(0.0, 30.0, 0.0).camaraPoint().apply { delay = 20 })
                    .addPoint(randomLoc(source.eyeLocation.add(0.0, 30.0, 0.0)).camaraPoint())
                    .addPoint(randomLoc(source.eyeLocation.add(0.0, 30.0, 0.0)).camaraPoint())
                    .addPoint(randomLoc(source.eyeLocation.add(0.0, 30.0, 0.0)).camaraPoint())
                    .addPoint(randomLoc(source.eyeLocation.add(0.0, 30.0, 0.0)).camaraPoint())
                    .addPoint(randomLoc(source.eyeLocation.add(0.0, 30.0, 0.0)).camaraPoint())
                    .addPoint(randomLoc(source.eyeLocation.add(0.0, 30.0, 0.0)).camaraPoint())
                    .addPoint(randomLoc(source.eyeLocation.add(0.0, 30.0, 0.0)).camaraPoint())
                    .play(source)


            }.register(this)

    }


    fun randomLoc(loc: Location) = loc.clone().add(Random.nextDouble(-25.0, 25.0), Random.nextDouble(-25.0, 25.0), Random.nextDouble(-25.0, 25.0)).apply {
        yaw = Random.nextDouble(-175.0, 175.0).toFloat()
        pitch = Random.nextDouble(-85.0, 85.0).toFloat()
    }

    override fun onDisable() {

    }

}