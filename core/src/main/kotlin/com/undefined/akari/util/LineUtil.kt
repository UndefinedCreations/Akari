package com.undefined.akari.util

import com.undefined.akari.entity.NMSCamera.createEntity
import com.undefined.akari.entity.NMSCamera.createServerEntity
import com.undefined.akari.entity.NMSCamera.spawnForClient
import com.undefined.akari.manager.NMSManager
import com.undefined.akari.player.CameraEntity
import com.undefined.akari.player.PlayerList
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Transformation
import org.bukkit.util.Vector
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.asin
import kotlin.math.atan2

object LineUtil {

    private val material: List<Material> =
        listOf(
            Material.WHITE_CONCRETE,
            Material.ORANGE_CONCRETE,
            Material.MAGENTA_CONCRETE,
            Material.LIGHT_BLUE_CONCRETE,
            Material.YELLOW_CONCRETE,
            Material.LIME_CONCRETE,
            Material.PINK_CONCRETE,
            Material.GRAY_CONCRETE,
            Material.LIGHT_GRAY_CONCRETE,
            Material.CYAN_CONCRETE,
            Material.PURPLE_CONCRETE,
            Material.BLUE_CONCRETE,
            Material.BROWN_CONCRETE,
            Material.GREEN_CONCRETE,
            Material.RED_CONCRETE,
            Material.BLACK_CONCRETE
        )

    fun randomMaterial() = material.random()

    fun createLine(players: MutableSet<Player>, loc1: Location, loc2: Location, material: Material) {
        val startLoc = loc1.clone().apply {
            yaw = 0f
            pitch = 0f
        }
        val direction: Vector = loc2.toVector().subtract(startLoc.toVector())
        val length = direction.length().toFloat()

        if (length < 0.001f) return

        val normalizedDirection = direction.clone().normalize()

        val yaw = atan2(normalizedDirection.x, normalizedDirection.z).toFloat()
        val pitch = asin(normalizedDirection.y).toFloat()

        val scale = Vector3f(0.2f, 0.2f, length)
        val world = loc1.world ?: return

        val rotation = Quaternionf()
            .rotateY(yaw)
            .rotateX(-pitch)

        val display = NMSManager.nms.createBlockDisplay(world)
        NMSManager.nms.setBlockDisplayBlock(display, material.createBlockData())
        NMSManager.nms.setEntityLocation(display, startLoc)
        NMSManager.nms.setTransformation(display, Vector3f(), rotation, scale, Quaternionf())

        val serverEntity = createServerEntity(display, world)
        players.forEach { spawnForClient(display, serverEntity, it) }

        NMSManager.nms.sendEntityData(display, players.toList())
    }
}