package com.undefined.akari

import com.undefined.akari.algorithm.AlgorithmType
import com.undefined.akari.camaraPath.CameraPath
import com.undefined.akari.camaraPath.CameraPoint
import com.undefined.lynx.logger.sendWarn
import com.undefined.stellar.StellarCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.util.Vector

object TestCommand {

    fun register () {
        val main = StellarCommand("test")
            .addAlias("t")
            .setDescription("Test command")
            .addExecution<CommandSender> {
                val player = sender as? Player ?: return@addExecution sendWarn("<red>Only players can use this command.")
                player.sendMessage("Test command executed.")

                CameraSequence(player.world)
                    .addCameraPath(
                        CameraPath()
                            .setAlgorithm(AlgorithmType.LERP)
                            .addCamaraPoint(CameraPoint(Vector(0.0, 0.0, 0.0), 0f, 0f))
                            .addCamaraPoint(CameraPoint(Vector(0.0, 30.0, 0.0), 0f, 0f))
                            .addCamaraPoint(CameraPoint(Vector(0.0, 70.0, 0.0), 0f, 0f))
                            .calculatePoints()
                    )
                    .play(player)



                player.sendMessage("Camera path calculated.")
           }
        main.register()
    }

}