package com.undefined.akari

import com.undefined.akari.algorithms.CamaraAlgorithm
import com.undefined.akari.exception.DifferentWorldException
import com.undefined.akari.manager.DisplayManager
import com.undefined.akari.objects.CamaraPoint
import org.bukkit.Material
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.joml.Vector3f
import java.util.*

class CamaraSequence(
    private val javaPlugin: JavaPlugin
) {

    private var pointsMap: SortedMap<Int, CamaraPoint> = sortedMapOf()

    fun addPoint(point: CamaraPoint): CamaraSequence {
        pointsMap.values.random().world?.let {
            if (point.world != it) throw DifferentWorldException(it, point.world!!)
        }

        val lastEntry = pointsMap.lastEntry()
        if (lastEntry != null) {
            val stillTick = lastEntry.key + lastEntry.value.delay + point.durationIntoPoint
            pointsMap[stillTick] = point
        } else {
            pointsMap[0] = point
        }

        return this
    }


    private fun generatePath(): SortedMap<Int, CamaraPoint> {
        //Moving all points down if needed
        pointsMap = moveFirstPointToFirstPlace()

        val generatedPathMap: SortedMap<Int, CamaraPoint> = sortedMapOf()
        
        var previousMainPoint: Pair<Int, CamaraPoint>? = null
        
        pointsMap.forEach { 

            //Checks its not the first point
            previousMainPoint?.let { previous ->
                generatedPathMap.putAll(//Generate Path from algorithm
                    CamaraAlgorithm.generate(
                        previous.first + previous.second.delay, previous.second, it.value
                    )
                )
            }

            //Add the main place
            for (x in 0..it.value.delay) {
                generatedPathMap[it.key + x] = it.value
            }

            previousMainPoint = Pair(it.key, it.value)
        }

        return generatedPathMap
    }

    private fun moveFirstPointToFirstPlace(): SortedMap<Int, CamaraPoint> {
        if (pointsMap.firstKey() == 0) return pointsMap

        val firstTick = pointsMap.firstKey()
        val newMap: SortedMap<Int, CamaraPoint> = sortedMapOf()
        pointsMap.forEach { newMap[it.key - firstTick] = it.value }
        return newMap
    }

    fun play(players: List<Player>) {

        val path = generatePath()

        path.forEach {

            val dis = it.value.world?.spawn(it.value, BlockDisplay::class.java)!!
            dis.block = Material.GREEN_CONCRETE.createBlockData()

           dis.displayWidth = 0.5f
            dis.displayHeight = 0.5f
        }

        val display = DisplayManager.create()

        display.spawn(path.firstEntry().value, players)
        display.setViewers(players)

        var tick = 0

        object : BukkitRunnable() {

            override fun run() {
                if (!path.containsKey(tick)) {
                    cancel()
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