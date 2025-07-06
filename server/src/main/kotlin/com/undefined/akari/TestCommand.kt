package com.undefined.akari

import com.undefined.akari.algorithm.AlgorithmType
import com.undefined.akari.camaraPath.CameraPath
import com.undefined.akari.camaraPath.point.toCameraPoint
import com.undefined.akari.player.CameraPlayer
import com.undefined.akari.player.CameraSequence
import com.undefined.lynx.logger.sendWarn
import com.undefined.stellar.StellarCommand
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.util.Vector

object TestCommand {

    fun register () {
        val main = StellarCommand("fly")
            .setDescription("Test command")
            .addExecution<Player> {
                val player = sender
                player.sendMessage("Test command executed.")

                CameraPlayer(player.world) {

                    setCameraSequence(
                        CameraSequence {
                            addCameraPath(CameraPath {
                                addCamaraPoint(
                                    player.eyeLocation.toCameraPoint().addPosition(Vector(0,0,0)), 80
                                )
                                addCamaraPoint(
                                    player.eyeLocation.toCameraPoint().addPosition(Vector(0,15,0)).setPitch(-90f), 80
                                )
                                setAlgorithm(AlgorithmType.LERP)
                            }.calculatePoints())
                        }
                    )
                    start()
                    addPlayer(player)
                }

                player.sendMessage("Camera path calculated.")
           }
        main.register()
    }

}