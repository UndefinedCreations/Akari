package com.undefined.akari.util

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.BlockDisplay
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

    fun createLine(loc1: Location, loc2: Location, material: Material) {
        val startLoc = loc1.clone().apply {
            yaw = 0f
            pitch = 0f
        }

        val direction: Vector = loc2.toVector().subtract(startLoc.toVector())
        val length = direction.length().toFloat()
        val normalizedDirection = direction.clone().normalize()

        val yaw = atan2(normalizedDirection.x, normalizedDirection.z).toFloat()
        val pitch = asin(normalizedDirection.y).toFloat()

        val scale = Vector3f(.2f, .2f, length)
        val display = loc1.world!!.spawn(startLoc, BlockDisplay::class.java)

        display.block = material.createBlockData()

        val rotation: Quaternionf = Quaternionf()
            .rotateY(yaw)
            .rotateX(pitch * -1)

        val transformation = Transformation(
            Vector3f(),
            rotation,
            scale,
            Quaternionf()
        )

        display.transformation = transformation
    }

}