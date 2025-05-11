package com.undefined.akari

import com.undefined.akari.algorithm.AlgorithmType
import com.undefined.akari.algorithm.lerp.LerpAlgorithm
import com.undefined.akari.algorithm.lerp.ShapingFunction
import com.undefined.akari.camaraPath.CameraPath
import com.undefined.akari.camaraPath.CameraPoint
import com.undefined.lynx.LynxConfig
import com.undefined.stellar.StellarConfig
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.Vector

class Main : JavaPlugin() {

    override fun onEnable() {
        AkariConfig
            .setPlugin(this)
        LynxConfig
            .setPlugin(this)
        StellarConfig
            .setPlugin(this)

        TestCommand.register()
    }
}


