@file:Suppress("SYNTHETIC_PROPERTY_WITHOUT_JAVA_ORIGIN")

package com.undefined.akari.player

import com.undefined.akari.algorithm.AlgorithmType
import com.undefined.akari.camaraPath.CalculatedPath
import com.undefined.akari.camaraPath.CameraPath
import com.undefined.akari.entity.NMSCamera.createServerEntity
import com.undefined.akari.entity.NMSCamera.spawnForClient
import com.undefined.akari.manager.NMSManager
import com.undefined.akari.util.LineUtil
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.joml.Quaternionf
import org.joml.Vector2f
import org.joml.Vector3f
import java.net.URI
import java.util.SortedMap
import java.util.UUID
import kotlin.collections.mutableSetOf
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class CameraSequence(
    kotlinDSL: CameraSequence.() -> Unit = {}
) {

    internal val pathMap: SortedMap<Int, CalculatedPath> = sortedMapOf()
    private var bridgeAlgorithm: AlgorithmType = AlgorithmType.INSTANT

    init {
        kotlinDSL(this)
    }

    /**
     * Sets smoothing algorithm for merging paths and return the modified [CameraSequence].
     */
    fun setBridgeAlgorithm(algorithmType: AlgorithmType): CameraSequence = apply {
        this.bridgeAlgorithm = algorithmType
    }

    /**
     * Add path at the end of the path list
     *
     * @param calculatedPath
     * @param time
     * @return
     */
    fun addCameraPath(calculatedPath: CalculatedPath, time: Int = 20): CameraSequence = apply {
        if (!pathMap.isEmpty() && bridgeAlgorithm != AlgorithmType.INSTANT) addBridgePath(calculatedPath, time)
        pathMap[pathMap.size] = calculatedPath
    }

    private fun addBridgePath(calculatedPath: CalculatedPath, time: Int) {
        val points = pathMap.lastEntry().value.calculatedPoints.values.toList()
        val lastPoint = points.last()
        val secondToLastPoint = points[points.size - 2]

        val firstNext = calculatedPath.calculatedPoints.values.first()
        val secondNext = calculatedPath.calculatedPoints.values.toList()[1]

        val bridgePath: CalculatedPath = CameraPath()
            .setAlgorithm(bridgeAlgorithm)
            .addCamaraPoint(secondToLastPoint, 0)
            .addCamaraPoint(lastPoint, 0)
            .addCamaraPoint(firstNext, time)
            .addCamaraPoint(secondNext, 0)
            .calculatePoints()

        //TODO Make this smoother
        pathMap[pathMap.size] = bridgePath

    }

    fun firstLocation(world: World): Location = pathMap.firstEntry().value.calculatedPoints.values.first().toLocation(world)

    fun spawnDisplayLine(players: MutableSet<Player>, world: World) {
        var past: Location? = null
        pathMap.forEach {
            val material = LineUtil.randomMaterial()
            for (point in it.value.calculatedPoints.values) {
                if (past == null) {
                    past = point.toLocation(world)
                    continue
                }
                val newLoc = point.toLocation(world)
                LineUtil.createLine(players, past, newLoc, material)
                past = newLoc
            }
        }
    }

    fun spawnPathPointDisplay(players: MutableSet<Player>, world: World) {
        pathMap.values.forEach { path ->
            path.originalPoints.values.forEach { point ->
                val yaw = Math.toRadians(-point.yaw.toDouble()).toFloat()
                val pitch = Math.toRadians(-point.pitch.toDouble()).toFloat()

                val rotation = Quaternionf()
                    .rotateY(yaw)
                    .rotateX(pitch)

                val head = ItemStack(Material.PLAYER_HEAD)
                val meta = head.itemMeta as SkullMeta
                val profile = Bukkit.createPlayerProfile(UUID.randomUUID())
                val textures = profile.textures
                textures.skin = URI("https://textures.minecraft.net/texture/e38e38187e5e90ca8f07d6035b15d817c6fdfdb69816f17fe097b1c6b004e507").toURL()
                profile.setTextures(textures)
                meta.ownerProfile = profile
                head.itemMeta = meta

                val loc = point.toLocation(world).apply {
                    this.yaw = 0f
                    this.pitch = 0f
                }

                val itemDisplay = NMSManager.nms.createItemDisplay(world)
                NMSManager.nms.setItemDisplayItem(itemDisplay, head)
                NMSManager.nms.setEntityLocation(itemDisplay, loc)
                NMSManager.nms.setTransformation(itemDisplay, Vector3f(), rotation, Vector3f(1f, 1f, 1f), Quaternionf())

                val serverEntity = createServerEntity(itemDisplay, world)
                players.forEach { player -> spawnForClient(itemDisplay, serverEntity, player) }

                NMSManager.nms.sendEntityData(itemDisplay, players.toList())
            }
        }
    }
}