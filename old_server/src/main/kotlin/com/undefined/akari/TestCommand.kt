package com.undefined.akari

import com.undefined.akari.algorithm.AlgorithmType
import com.undefined.akari.camaraPath.CameraPath
import com.undefined.akari.camaraPath.point.toCameraPoint
import com.undefined.akari.camaraPath.presetPath.OrbitalPath
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
            .addPhraseArgument("locations")
            .addExecution<Player> {
                val rawLocations = getArgument<String>("locations")
                val player = sender

                player.sendMessage("Parsing locations: $rawLocations")

                // Parse input: "0,0,0 0,15,0 0,40,20"
                val vectors = rawLocations
                    .trim()
                    .split(" ")
                    .mapNotNull { part ->
                        val nums = part.split(",")
                        if (nums.size == 3) {
                            nums.mapNotNull { it.toDoubleOrNull() }.let {
                                if (it.size == 3) Vector(it[0], it[1], it[2]) else null
                            }
                        } else null
                    }

                if (vectors.isEmpty()) {
                    player.sendMessage("No valid locations provided!")
                    return@addExecution
                }

                CameraPlayer(player.world) {
                    setCameraSequence(
                        CameraSequence {
                            addCameraPath(CameraPath {
                                vectors.forEach { vec ->
                                    addCamaraPoint(
                                        player.eyeLocation.toCameraPoint().addPosition(vec),
                                        80 // duration per point
                                    )
                                }
                                setAlgorithm(AlgorithmType.BSPLINE)
                                spawnDisplayLine(player.world)
                                setBukkitCamera(false)
                            }.calculatePoints())
                        }
                    )
                    start()
                    addPlayer(player)
                }

                player.sendMessage("Camera path calculated with ${vectors.size} points.")
           }
        main.register()

        val orbit = StellarCommand("orbit")
            .addFloatArgument("radius")
            .addDoubleArgument("height")
            .addIntegerArgument("time")
            .addExecution<Player> {

                val radius: Float by args
                val height: Double by args
                val time: Int by args

                CameraPlayer(sender.world) {
                    setCameraSequence(
                        CameraSequence {
                            addCameraPath(
                                OrbitalPath(
                                    center = sender.location,
                                    radius = radius,
                                    height = height,
                                    time = time,
                                ).calculatePoints()
                            )
                            setBukkitCamera(false)
                        }
                    )
                    start()
                    addPlayer(sender)
                }

                sender.sendMessage("Orbiting!!")
            }
            .register()
    }

}