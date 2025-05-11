package com.undefined.akari

import com.undefined.akari.algorithm.AlgorithmType
import com.undefined.akari.camaraPath.CameraPath
import com.undefined.akari.camaraPath.CameraPoint
import com.undefined.akari.manager.NMSManager
import com.undefined.lynx.logger.sendInfo
import com.undefined.lynx.logger.sendWarn
import com.undefined.stellar.StellarCommand
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Display
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class TestCommand {

    fun register () {
        val main = StellarCommand("test")
            .addAlias("t")

            .setDescription("Test command")
            .addArgument(name = "a")
            .addExecution<CommandSender> {
                val player = sender as? Player ?: run {
                    sendWarn("<red>Only players can use this command.")
                    return@addExecution
                }
                player.sendMessage("<green>Test command executed.")

                val cameraPath = CameraPath()
                    .setAlgorithm(AlgorithmType.LERP)
                    .addCamaraPoint(CameraPoint(Vector(0.0, 0.0, 0.0), 0f, 0f), 20)
                    .addCamaraPoint(CameraPoint(Vector(0.0, 30.0, 0.0), 0f, 0f), 20)
                    .addCamaraPoint(CameraPoint(Vector(0.0, 70.0, 0.0), 0f, 0f), 20)
                    .calculatePoints()

                player.sendMessage("<green>Camera path calculated.")
            }
        main.register()
    }

}