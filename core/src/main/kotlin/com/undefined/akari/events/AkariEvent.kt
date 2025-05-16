package com.undefined.akari.events

import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

open class AkariEvent : Event() {

    override fun getHandlers(): HandlerList = HandlerList()

    fun call() = Bukkit.getPluginManager().callEvent(this)
}