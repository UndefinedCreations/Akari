package com.undefined.akari

import com.undefined.akari.algorithm.lerp.LerpAlgorithm
import com.undefined.akari.algorithm.lerp.ShapingFunction
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {

        AkariConfig()
            .setPlugin(this)


        CamaraPath()
            .setAlg(LerpAlgorithm(ShapingFunction.PARAMETRIC_BLEND))
            .addLocation()
            .addCamaraPath(CamaraPath())
            .add
            .addLocation()
            .addLocation()


    }
}

class CamaraPath() {

    val points: HashMap<Int, Location> = hashMapOf()

    fun addLocation(location: Location, time: Int = 20): CamaraPath {
        val highestPoint = points.keys.maxOf { it }
        points[highestPoint+time] = location
        return this
    }

    fun addCamaraPath(camaraPath: CamaraPath) {}


    fun play(playerList: List<Player>) {

    }

    fun play(vararg players: Player) {
        play(players.toList())
    }

    fun play(player: Player) = play(listOf(player))

}


