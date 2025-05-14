@file:Suppress("NO_REFLECTION_IN_CLASS_PATH", "SYNTHETIC_PROPERTY_WITHOUT_JAVA_ORIGIN")

package com.undefined.akari

import com.undefined.akari.algorithm.AlgorithmType
import com.undefined.akari.camaraPath.CalculatedPath
import com.undefined.akari.camaraPath.CameraPath
import com.undefined.akari.camaraPath.CameraPoint
import com.undefined.akari.entity.BukkitCamera
import com.undefined.akari.entity.Camera
import com.undefined.akari.entity.NMSCamera
import com.undefined.akari.util.LineUtil
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.SortedMap

class CameraSequence(
    private val world: World
) {

    private val pathMap: SortedMap<Int, CalculatedPath> = sortedMapOf()
    private var algorithm: AlgorithmType = AlgorithmType.INSTANT

    private var camera: Camera = NMSCamera

    fun setBukkitCamera(bukkit: Boolean): CameraSequence = apply {
        camera = if (bukkit) BukkitCamera else NMSCamera
    }

    /**
     * Sets smoothing algorithm for merging paths and return the modified [CameraSequence].
     */
    fun setBridgeAlgorithm(algorithmType: AlgorithmType): CameraSequence = apply {
        this.algorithm = algorithmType
    }

    /**
     * Add path at the end of the path list
     *
     * @param calculatedPath
     * @param time
     * @return
     */
    fun addCameraPath(calculatedPath: CalculatedPath, time: Int = 20): CameraSequence = apply {
        if (!pathMap.isEmpty && algorithm != AlgorithmType.INSTANT) addBridgePath(calculatedPath, time)
        pathMap[pathMap.size] = calculatedPath
    }

    private fun addBridgePath(calculatedPath: CalculatedPath, time: Int) {
        val points = pathMap.lastEntry().value.calculatedPoints.values.toList()
        val lastPoint = points.last()
        val secondToLastPoint = points[points.size - 2]

        val firstNext = calculatedPath.calculatedPoints.values.first()
        val secondNext = calculatedPath.calculatedPoints.values.toList()[1]

        val bridgePath: CalculatedPath = CameraPath()
            .setAlgorithm(algorithm)
            .addCamaraPoint(secondToLastPoint, 0)
            .addCamaraPoint(lastPoint, 0)
            .addCamaraPoint(firstNext, time)
            .addCamaraPoint(secondNext, 0)
            .calculatePoints()

        //TODO Make this smoother
        pathMap[pathMap.size] = bridgePath

    }


    fun play(players: List<Player>) {
        if (players.isEmpty()) throw IllegalArgumentException("Players can't be empty")
        if (pathMap.isEmpty()) throw IllegalArgumentException("Camera path can't be empty")

        val finalPathMap = pathMap.values.flatMap { it.calculatedPoints.values }

        val startLoc = pathMap.firstEntry().value.calculatedPoints.values.first().toLocation(world)

        val entity = camera.spawn(world, startLoc, players)
        camera.setInterpolationDuration(entity, 1, players)
        camera.setCamera(entity, players)

        object : BukkitRunnable() {
            var index = 0

            init {
                var past: Location? = null

                println(pathMap.size)

                pathMap.forEach {
                    val material = LineUtil.randomMaterial()
                    for (point in it.value.calculatedPoints.values) {
                        if (past == null) {
                            past = point.toLocation(world)
                            continue
                        }
                        val newLoc = point.toLocation(world)
//                        LineUtil.createLine(past, newLoc, material)
                        past = newLoc
                    }
                }
            }

            override fun run() {
                if (index >= finalPathMap.size) {
                    cancel()
                    return
                }
                val point: CameraPoint? = finalPathMap[index]
                if (point == null) {
                    camera.removeCamera(players)
                    throw RuntimeException("Next point not found.")
                }
                camera.teleport(entity, point.toLocation(world), players)
                players.forEach { it.sendMessage("Moved $index") }
                index++
            }

            override fun cancel() {
                super.cancel()
                camera.removeCamera(players)
                camera.kill(entity, players)
            }
        }.runTaskTimer(AkariConfig.javaPlugin, 1, 1)
    }

    fun play(player: Player) = play(listOf(player))

}