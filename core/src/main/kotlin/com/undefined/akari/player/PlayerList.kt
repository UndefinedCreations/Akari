package com.undefined.akari.player

import com.undefined.akari.entity.NMSCamera
import com.undefined.akari.manager.GamemodeManager
import com.undefined.akari.manager.NMSManager
import org.bukkit.GameMode
import org.bukkit.entity.Player
import kotlin.reflect.KProperty

class PlayerList(private val cameraPlayer: CameraPlayer, private var state: MutableSet<Player>) : MutableSet<Player> by state {

    override fun add(element: Player): Boolean {
        setAndSaveGameMode(element)
        if (isNMS()) sendPlayerClient(element) else setPlayerBukkit(element)
        return state.add(element)
    }

    override fun addAll(elements: Collection<Player>): Boolean {
        elements.forEach { setAndSaveGameMode(it) }
        if (isNMS()) elements.forEach { sendPlayerClient(it) } else elements.forEach { setPlayerBukkit(it) }
        return state.addAll(elements)
    }

    override fun remove(element: Player): Boolean {
        resetGameMode(element)
        if (isNMS()) removePlayerClient(element) else cameraPlayer.camera.removeCamera(listOf(element))
        return state.remove(element)
    }

    override fun removeAll(elements: Collection<Player>): Boolean {
        elements.forEach { resetGameMode(it) }
        if (isNMS()) elements.forEach { removePlayerClient(it) } else cameraPlayer.camera.removeCamera(elements.toList())
        return state.removeAll(elements)
    }

    override fun clear() {
        state.forEach { resetGameMode(it) }
        if (isNMS()) state.forEach { removePlayerClient(it) } else cameraPlayer.camera.removeCamera(state.toList())
        state.clear()
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): PlayerList = this
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: PlayerList) {
        state = value
    }

    private fun resetGameMode(player: Player) {
        if (cameraPlayer.exitGameMode == null) GamemodeManager.pastGameMode[player.uniqueId]?.run { player.gameMode = this } else player.gameMode = cameraPlayer.exitGameMode!!
        GamemodeManager.pastGameMode.remove(player.uniqueId)
    }

    private fun setAndSaveGameMode(player: Player) {
        GamemodeManager.pastGameMode[player.uniqueId] = player.gameMode
        player.gameMode = GameMode.SPECTATOR
    }

    private fun removePlayerClient(player: Player) {
        cameraPlayer.camera.removeCamera(listOf(player))
        cameraPlayer.camera.kill(cameraPlayer.cameraEntity!!.entity, listOf(player))
    }

    private fun sendPlayerClient(player: Player) {
        cameraPlayer.camera.setInterpolationDuration(cameraPlayer.cameraEntity!!.entity, 1, listOf(player))
        cameraPlayer.camera.spawnForClient(cameraPlayer.cameraEntity!!.entity, cameraPlayer.cameraEntity?.serverEntity, player)
        cameraPlayer.camera.setCamera(cameraPlayer.cameraEntity!!.entity, listOf(player))
    }


    private fun setPlayerBukkit(player: Player) {
        if (cameraPlayer.cameraEntity == null) return
        cameraPlayer.camera.setCamera(cameraPlayer.cameraEntity!!.entity, listOf(player))
    }

    private fun isNMS(): Boolean {
        if (cameraPlayer.cameraEntity == null) return false
        if (cameraPlayer.camera !is NMSCamera) return false
        return true
    }
}