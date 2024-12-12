package com.undefined.akari

import com.undefined.akari.algorithms.CamaraAlgorithm
import com.undefined.akari.exception.DifferentWorldException
import com.undefined.akari.manager.DisplayManager
import com.undefined.akari.objects.CamaraAlgorithmType
import com.undefined.akari.objects.CamaraPoint
import org.bukkit.Material
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.joml.Vector3f
import java.util.*

class CamaraSequence(
    private val javaPlugin: JavaPlugin,
    private val camaraAlgorithmType: CamaraAlgorithmType = CamaraAlgorithmType.SMOOTH
) {

    private var pointsMap: SortedMap<Int, CamaraPoint> = sortedMapOf()

    fun addPoint(point: CamaraPoint): CamaraSequence {
        pointsMap.values.randomOrNull()?.world?.let {
            if (point.world != it) throw DifferentWorldException(it, point.world!!)
        }

        val lastEntry = pointsMap.lastEntry()
        if (lastEntry != null) {
            val stillTick = lastEntry.key + lastEntry.value.delay + point.durationIntoPoint
            println("NExt point at $stillTick")
            pointsMap[stillTick] = point
        } else {
            println("STart At 0")
            pointsMap[0] = point
        }

        pointsMap.lastEntry().run {
            for (x in 0..value.delay) {
                println(key + x)
                pointsMap[key + x] = point
            }
        }

        return this
    }


    private fun generatePath(): SortedMap<Int, CamaraPoint> = CamaraAlgorithm.generate(camaraAlgorithmType, pointsMap)

    fun play(players: List<Player>) {

        val path = generatePath()
//
//        path.forEach {
//
//            val dis = it.value.world?.spawn(it.value, BlockDisplay::class.java)!!
//            dis.block = Material.GREEN_CONCRETE.createBlockData()
//
//           dis.displayWidth = 0.1f
//            dis.displayHeight = 0.1f
//        }

        val display = DisplayManager.create()

        display.spawn(path.firstEntry().value, players)
        display.setViewers(players)

        var tick = 0

        object : BukkitRunnable() {

            override fun run() {
                if (!path.containsKey(tick)) {
                    println(tick)
                    cancel()
                    display.removeViewers(players)
                    display.remove()
                    return
                }
                display.interpolationDuration(1)
                display.teleport(path[tick]!!)
                tick++
            }

        }.runTaskTimer(javaPlugin, 1, 1)

    }

    fun play(player: Player) = play(listOf(player))
}