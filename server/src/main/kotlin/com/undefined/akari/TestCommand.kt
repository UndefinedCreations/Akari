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
                player.sendMessage("Hello New World!")
                player.sendMessage("Test command executed.")

                CameraSequence(player.world)
                    .setBukkitCamera(false)
                    .addCameraPath(
                        CameraPath()
                            .setAlgorithm(AlgorithmType.CATMULLROM)
                            .addCamaraPoint(player.location.toCamaraPoint(),0)
                            .addCamaraPoint(player.location.toCamaraPoint().setYaw(90f).addPosition(Vector(10,2,5)), 60)
                            .addCamaraPoint(player.location.toCamaraPoint().setYaw(45f).addPosition(Vector(10,20,- 5)), 60)
                            .addCamaraPoint(player.location.toCamaraPoint().addPosition(Vector(0, 0, 0)), 60)
                            .addCamaraPoint(player.location.toCamaraPoint().addPosition(Vector(0, 0, 0)), 60)
                            .calculatePoints()
                    )
                    .setBridge(AlgorithmType.SMOOTHSTEP)
                    .addCameraPath(
                        CameraPath()
                          .setAlgorithm(AlgorithmType.CATMULLROM)
                          .addCamaraPoint(CameraPoint(Vector(100, 100, 100), 45f, 45f), 60)
                          .addCamaraPoint(player.location.toCamaraPoint().setYaw(90f).addPosition(Vector(10,2,5)), 60)
                          .addCamaraPoint(player.location.toCamaraPoint().setYaw(90f).setPitch(45f).addPosition(Vector(10,2,5)), 60)
                          .addCamaraPoint(player.location.toCamaraPoint().addPosition(Vector(0, 2, 0)), 60)
                          .calculatePoints()
                    )
                    .play(player)

                player.sendMessage("Camera path calculated.")
           }
        main.register()
    }

}