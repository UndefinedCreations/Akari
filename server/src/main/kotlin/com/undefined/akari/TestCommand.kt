package com.undefined.akari

import com.undefined.akari.algorithm.AlgorithmType
import com.undefined.akari.camaraPath.CameraPath
import com.undefined.akari.camaraPath.CameraPoint
import com.undefined.akari.camaraPath.toCamaraPoint
import com.undefined.lynx.logger.sendWarn
import com.undefined.stellar.StellarCommand
import com.undefined.stellar.argument.world.LocationType
import org.bukkit.Location
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
                    .showLines(false)
                    .setBukkitCamera(false)
                    .setBridgeAlgorithm(AlgorithmType.SMOOTHSTEP)
                    .addCameraPath(
                        CameraPath()
                            .setAlgorithm(AlgorithmType.BSPLINE)
                            .addCamaraPoint(player.location.toCameraPoint().addPosition(Vector(0, 5, 0)),60)
                            .addCamaraPoint(player.location.toCameraPoint().addPosition(Vector(0, 0, 0)),10)
                            .addCamaraPoint(player.location.toCameraPoint().addPosition(Vector(0, 5, 0)),10)
                            .addCamaraPoint(player.location.toCameraPoint().addPosition(Vector(0, 0, 0)),60)
                            .calculatePoints(),
                        100
                    )
                    .addCameraPath(
                        OrbitalPath(player.eyeLocation, 4f, 2.0, 60).calculatePoints())
                    .addCameraPath(
                        OrbitalPath(player.eyeLocation, 8f,6.0, 60).calculatePoints())
                    .play(player)

                player.sendMessage("Camera path calculated.")
           }
        main.register()

        val gta = StellarCommand("tpgta")
            .addAlias("gta")
            .setDescription("Test command")
            .addLocationArgument("location",LocationType.LOCATION_3D)
            .addExecution<Player> {
                val location: Location by args
                val height: Double = location.y + 100
                CameraSequence(sender.world)
                    .setEndingPoint(location.toCameraPoint())
                    .addCameraPath(
                        CameraPath()
                            .addCamaraPoint(sender.eyeLocation.toCameraPoint(),0)
                            .addCamaraPoint(sender.eyeLocation.toCameraPoint().setPosition(Vector(sender.eyeLocation.x, height, sender.eyeLocation.z)).setPitch(90f),60)
                            .addCamaraPoint(location.toCameraPoint().setPosition(location.x, height, location.z).setPitch(90f),60)
                            .addCamaraPoint(location.toCameraPoint(), 60)
                            .calculatePoints()
                    )
                    .play(sender)
            }
        gta.register()
    }


}