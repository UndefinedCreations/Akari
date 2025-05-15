package com.undefined.akari.player

import com.undefined.akari.entity.NMSCamera
import org.bukkit.entity.Player
import kotlin.reflect.KProperty

class PlayerList(private val cameraPlayer: CameraPlayer, private var state: MutableList<Player>) : MutableList<Player> by state {

    override fun add(element: Player): Boolean {
        if (isNMS()) sendPlayerClient(element)
        return state.add(element)
    }

    override fun addAll(elements: Collection<Player>): Boolean {
        if (isNMS()) elements.forEach { sendPlayerClient(it) }
        return state.addAll(elements)
    }

    override fun remove(element: Player): Boolean {
        if (isNMS()) removePlayer(element)
        return state.remove(element)
    }

    override fun removeAll(elements: Collection<Player>): Boolean {
        if (isNMS()) elements.forEach { removePlayer(it) }
        return state.removeAll(elements)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): PlayerList = this
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: PlayerList) {
        state = value
    }

    private fun removePlayer(player: Player) {
        cameraPlayer.camera.removeCamera(listOf(player))
        cameraPlayer.camera.kill(cameraPlayer.cameraEntity!!.entity, listOf(player))
    }

    private fun sendPlayerClient(player: Player) {
        cameraPlayer.camera.spawnForClient(cameraPlayer.cameraEntity!!.entity, cameraPlayer.cameraEntity?.serverEntity, player)
        cameraPlayer.camera.setCamera(cameraPlayer.cameraEntity!!.entity, listOf(player))
    }

    private fun isNMS(): Boolean {
        if (cameraPlayer.cameraEntity == null) return false
        if (cameraPlayer.camera !is NMSCamera) return false
        return true
    }
}