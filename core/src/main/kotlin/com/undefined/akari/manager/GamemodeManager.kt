package com.undefined.akari.manager

import com.undefined.akari.AkariConfig
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID

object GamemodeManager : Listener {

    val pastGameMode: HashMap<UUID, GameMode> = hashMapOf()

    init {
        Bukkit.getPluginManager().registerEvents(this, AkariConfig.javaPlugin)
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        pastGameMode[e.player.uniqueId]?.run { e.player.gameMode = this }
        pastGameMode.remove(e.player.uniqueId)
    }

}