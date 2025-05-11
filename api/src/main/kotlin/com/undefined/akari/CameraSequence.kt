@file:Suppress("NO_REFLECTION_IN_CLASS_PATH")

package com.undefined.akari

import com.undefined.akari.algorithm.Algorithm
import com.undefined.akari.algorithm.AlgorithmType
import com.undefined.akari.algorithm.lerp.LerpAlgorithm
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

class CameraSequence(
    private val world: World
) {

    private val pathMap: HashMap<CalculatedPath, Int> = hashMapOf()
    private var algorithm: Algorithm = LerpAlgorithm()

    private var camera: Camera = NMSCamera

    fun setBukkitCamera(bukkit: Boolean): CameraSequence = apply {
        camera = if (bukkit) BukkitCamera else NMSCamera
    }

    /**
     * Sets smoothing algorithm for merging paths and return the modified [CameraSequence].
     */
    fun setBridge(algorithmType: AlgorithmType): CameraSequence = apply {
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

    private fun getFullPath(): List<CameraPoint> {
        pathMap.keys.forEach { println(it.calculatedPoints) }
        return pathMap.keys.flatMap { it.calculatedPoints.values }
    }

    fun play(players: List<Player>) {
        if (players.isEmpty()) throw IllegalArgumentException("Players can't be empty")
        if (pathMap.isEmpty()) throw IllegalArgumentException("Camera path can't be empty")

        val startLoc = pathMap.keys.first().calculatedPoints.values.first().toLocation(world)

        val entity = camera.spawn(world, startLoc, players)
        camera.setInterpolationDuration(entity, 1, players)
        camera.setCamera(entity, players)

        object : BukkitRunnable() {
            var index = 0
            val fullPath = getFullPath()

            init {
                var past: Location? = null

                for (point in fullPath) {
                    if (past == null) {
                        past = point.toLocation(world)
                        continue
                    }
                    val newLoc = point.toLocation(world)
                    LineUtil.createLine(past, newLoc)
                    past = newLoc
                }
            }

            override fun run() {
                if (index >= fullPath.size) {
                    cancel()
                    return
                }
                val point: CameraPoint? = fullPath[index]
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