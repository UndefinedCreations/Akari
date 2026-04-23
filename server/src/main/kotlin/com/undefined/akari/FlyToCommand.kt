package com.undefined.akari

import com.undefined.akari.algorithm.AlgorithmType
import com.undefined.akari.camaraPath.CameraPath
import com.undefined.akari.camaraPath.point.toCameraPoint
import com.undefined.akari.player.CameraPlayer
import com.undefined.akari.player.CameraSequence
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class FlyToCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Only players can use this command!")
            return true
        }

        val targetBlock = sender.getTargetBlockExact(100)

        if (targetBlock == null) {
            sender.sendMessage("§cNo block in sight within 100 blocks!")
            return true
        }

        val from = sender.location.add(0.0, 2.0, 0.0)
        val to = targetBlock.location.add(0.5, 2.0, 0.5)

        // Calculate look direction from start to target
        val direction = to.clone().subtract(from).toVector()
        to.direction = direction

        sender.sendMessage("§aFlying to block at ${targetBlock.x}, ${targetBlock.y}, ${targetBlock.z}")

        val seq = CameraSequence {
            addCameraPath(
                CameraPath {
                    addCamaraPoint (
                        from.toCameraPoint()
                    )
                    addCamaraPoint(to.toCameraPoint(), 30)
                    addCamaraPoint(to.toCameraPoint().addPosition(Vector(15, 15, 35)), 30)
                    addCamaraPoint(to.toCameraPoint().addPosition(Vector(35, 15, 45)), 30)
                    addCamaraPoint(to.toCameraPoint().addPosition(Vector(12, 5, 25)), 30)
                    addCamaraPoint(to.toCameraPoint().addPosition(Vector(32, 55, 75)), 30)
                    addCamaraPoint(to.toCameraPoint(), 50)
                    addCamaraPoint(from.toCameraPoint(), 30)
                }
                    .setAlgorithm(AlgorithmType.BSPLINE)
                    .calculatePoints()
            )
        }
        seq.spawnDisplayLine(mutableSetOf<Player>(sender), sender.world)
        seq.spawnPathPointDisplay(mutableSetOf(sender), sender.world)

        CameraPlayer(sender.world) {
            setCameraSequence(seq).setBukkitCamera(false)
            start()
            addPlayer(sender)
        }

        return true
    }
}