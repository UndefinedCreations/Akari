package com.undefined.akari

import com.undefined.akari.algorithm.AlgorithmType
import com.undefined.akari.camaraPath.CameraPath
import com.undefined.akari.camaraPath.point.toCameraPoint
import com.undefined.akari.camaraPath.presetPath.OrbitalPath
import com.undefined.akari.display.TextElement
import com.undefined.akari.manager.NMSManager
import com.undefined.akari.player.CameraPlayer
import com.undefined.akari.player.CameraSequence
import com.undefined.lynx.logger.sendWarn
import com.undefined.stellar.StellarCommand
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Display
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

//                val calPath = CameraPath {
//                    addCamaraPoint(
//                        x = 10.0
//                    )
//                    addCamaraPoint(
//                        x = 0.5
//                    )
//                    addCamaraPoint(
//                        x = 25.0
//                    )
//                }.calculatePoints()
//
//                CameraPlayer(player.world) {
//
//                    setCameraSequence(
//                        CameraSequence {
//                            addCameraPath(
//                                OrbitalPath(player.location, 0.0, time = 200).calculatePoints()
//                            )
//                        }
//                    )
//
//                    addDisplayElement(TextElement(0, 200, "Testing"))
//                    start()
//                    addPlayer(player)
//                }


                val ak = NMSManager.nms.createTextDisplay(player.world)
                NMSManager.nms.setEntityLocation(ak, player.location.apply {
                    this.yaw = 0f
                    this.pitch = 0f
                })
                val se = NMSManager.nms.createServerEntity(ak, player.world)

                NMSManager.nms.sendSpawnPacket(ak, se, listOf(player))
                NMSManager.nms.setBillboard(ak, Display.Billboard.CENTER, listOf(player))
                NMSManager.nms.setTextTextDisplay(ak, "Testing", listOf(player))

                NMSManager.nms.setTransformation(ak, Vector(0.0, 0.0, 5.0), listOf(player))

                println(player.location)

                player.sendMessage("Camera path calculated.")
           }
        main.register()
    }

}