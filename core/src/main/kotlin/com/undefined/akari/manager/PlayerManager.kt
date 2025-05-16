package com.undefined.akari.manager

import com.undefined.akari.player.CameraPlayer
import org.bukkit.entity.Player

object PlayerManager {
    private val players: List<CameraPlayer> = listOf<CameraPlayer>()

    fun addPlayer (player: CameraPlayer) {
        players.toMutableList().add(player)
    }
    fun removePlayer (player: CameraPlayer) {
        players.toMutableList().remove(player)
    }
    fun getPlayer (player: Player): CameraPlayer? {
        return players.find { it.getPlayers().contains(player) }
    }
    fun getAllPlayers (): List<CameraPlayer> {
        return players
    }
}