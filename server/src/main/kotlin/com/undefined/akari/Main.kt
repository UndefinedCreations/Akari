package com.undefined.akari

import com.undefined.akari.algorithm.lerp.LerpAlgorithm
import com.undefined.akari.algorithm.lerp.ShapingFunction
import com.undefined.akari.camaraPath.CameraPath
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {

        AkariConfig()
            .setPlugin(this)


        CameraPath()
            .setAlgorithm(LerpAlgorithm())

    }
}


