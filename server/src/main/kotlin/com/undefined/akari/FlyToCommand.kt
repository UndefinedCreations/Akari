package com.undefined.akari

import com.undefined.akari.algorithm.AlgorithmType
import com.undefined.akari.camaraPath.CameraPath
import com.undefined.akari.camaraPath.point.toCameraPoint
import com.undefined.akari.camaraPath.presetPath.OrbitalPath
import com.undefined.akari.player.CameraPlayer
import com.undefined.akari.player.CameraSequence
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

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
                    setAlgorithm(AlgorithmType.SMOOTHSTEP)
                    addCamaraPoint (
                        from.toCameraPoint()
                    )
                    addCamaraPoint(to.toCameraPoint(), 60)
                }.calculatePoints()
            )
        }
        seq.spawnDisplayLine(sender.world)

        CameraPlayer(sender.world) {
            setCameraSequence(seq).setBukkitCamera(false)
            start()
            addPlayer(sender)
        }

        return true
    }
}