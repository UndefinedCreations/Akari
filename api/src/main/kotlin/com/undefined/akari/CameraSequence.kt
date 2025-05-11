@file:Suppress("NO_REFLECTION_IN_CLASS_PATH")

package com.undefined.akari

import com.undefined.akari.algorithm.Algorithm
import com.undefined.akari.algorithm.AlgorithmType
import com.undefined.akari.algorithm.lerp.LerpAlgorithm
import com.undefined.akari.camaraPath.CalculatedPath
import com.undefined.akari.camaraPath.CameraPoint
import com.undefined.akari.manager.NMSManager
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class CameraSequence(
    private val world: World
) {

    private val pathMap: HashMap<CalculatedPath, Int> = hashMapOf()
    private var algorithm: Algorithm = LerpAlgorithm()

    /**
     * Sets smoothing algorithm for merging paths and return the modified [CameraSequence].
     */
    fun setAlgorithm(algorithmType: AlgorithmType): CameraSequence = apply {
        this.algorithm = algorithmType.klass.constructors.first().call()
    }

    /**
     * Add path at the end of the path list
     *
     * @param calculatedPath
     * @param time
     * @return
     */
    fun addCameraPath(calculatedPath: CalculatedPath, time: Int = 20): CameraSequence = apply {
        pathMap[calculatedPath] = time
    }

    private fun getFullPath(): HashMap<Int, CameraPoint> {

        return pathMap.map { it.key.calculatedPoints }
            .reduce { acc, calculatedPath ->
                val merged = hashMapOf<Int, CameraPoint>()
                acc.forEach { (tick, point) ->
                    merged[tick] = point
                }
                calculatedPath.forEach { (tick, point) ->
                    merged[tick] = point
                }
                merged
            }
    }

    fun play(players: List<Player>) {
        if (players.isEmpty()) throw IllegalArgumentException("Players can't be empty")
        if (pathMap.isEmpty()) throw IllegalArgumentException("Camera path can't be empty")

        val entity = NMSManager.nms.createItemDisplay(world)
        NMSManager.nms.setEntityLocation(entity, pathMap.keys.first().calculatedPoints.values.first().toLocation(world))
        val serverEntity = NMSManager.nms.createServerEntity(entity, world)
        NMSManager.nms.sendSpawnPacket(entity, serverEntity, players)
        NMSManager.nms.setInterpolationDuration(entity, 1, players)
        NMSManager.nms.sendSetCameraPacket(entity, players)



        object : BukkitRunnable() {
            var index = 0
            val fullPath = getFullPath()
            override fun run() {
                if (index >= pathMap.values.size) {
                    cancel()
                    return
                }
                val point: CameraPoint? = fullPath[index]
                if (point == null) {
                    NMSManager.nms.sendRemoveEntityPacket(entity, players)
                    throw RuntimeException("Next point not found.")
                }
                NMSManager.nms.setEntityLocation(entity, point.toLocation(world))
                NMSManager.nms.sendTeleportPacket(entity, players)
                players.forEach { it.sendMessage("Moved $index") }
                index++
            }

        }.runTaskTimer(AkariConfig.javaPlugin, 1, 1)


    }

    fun play(player: Player) = play(listOf(player))

}