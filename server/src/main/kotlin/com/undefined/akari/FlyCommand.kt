package com.undefined.akari

import com.undefined.akari.camaraPath.presetPath.OrbitalPath
import com.undefined.akari.player.CameraPlayer
import com.undefined.akari.player.CameraSequence
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FlyCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Only players can use this command!")
            return true
        }

        val radius: Float = 10.0f
        val height: Double = 2.0
        val time: Int = 120

                val seq = CameraSequence {
                    addCameraPath(
                        OrbitalPath(
                            center = sender.location,
                            radius = radius,
                            height = height,
                            time = time,
                        ).calculatePoints()
                    )
                }
                seq.spawnDisplayLine(mutableSetOf<Player>(sender), sender.world)

        CameraPlayer(sender.world) {
            setCameraSequence(seq).setBukkitCamera(false)
            start()
            addPlayer(sender)
        }

        sender.sendMessage("Test command ran")
        return true
    }
}