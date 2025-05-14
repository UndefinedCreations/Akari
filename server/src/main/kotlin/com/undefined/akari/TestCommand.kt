package com.undefined.akari

import com.undefined.akari.algorithm.AlgorithmType
import com.undefined.akari.camaraPath.CameraPath
import com.undefined.akari.camaraPath.CameraPoint
import com.undefined.akari.camaraPath.toCamaraPoint
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
                    .setBukkitCamera(false)
                    .addCameraPath(
                        CameraPath()
                            .setAlgorithm(AlgorithmType.BSPLINE)
                            .addCamaraPoint(player.location.toCameraPoint().addPosition(Vector(0, 5, 0)),60)
                            .addCamaraPoint(player.location.toCameraPoint().addPosition(Vector(0, 0, 0)),0)
                            .addCamaraPoint(player.location.toCameraPoint().addPosition(Vector(0, 0, 0)).setYaw(90f).addPosition(Vector(10,2,5)), 60)
                            .addCamaraPoint(player.location.toCameraPoint().addPosition(Vector(0, 0, 0)).setYaw(90f).setPitch(70f).addPosition(Vector(40,0,5)), 60)
                            .addCamaraPoint(player.location.toCameraPoint().addPosition(Vector(0, 0, 0)).setYaw(30f).setPitch(70f).addPosition(Vector(10,0,50)), 60)
                            .addCamaraPoint(player.location.toCameraPoint().addPosition(Vector(0, 0, 0)).setYaw(90f).addPosition(Vector(10,2,5)), 60)
                            .addCamaraPoint(player.location.toCameraPoint().addPosition(Vector(0, 5, 0)),60)
                            .addCamaraPoint(player.location.toCameraPoint().addPosition(Vector(0, 0, 0)),60)
                            .calculatePoints(),
                        100
                    )
                    .play(player)

                player.sendMessage("Camera path calculated.")
           }
        main.register()
    }

}