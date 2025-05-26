package com.undefined.akari.player

import com.undefined.akari.entity.NMSCamera
import com.undefined.akari.manager.GamemodeManager
import com.undefined.akari.manager.NMSManager
import org.bukkit.GameMode
import org.bukkit.entity.Display
import org.bukkit.entity.Player
import kotlin.reflect.KProperty

class PlayerList(private val cameraPlayer: CameraPlayer, private var state: MutableSet<Player>) : MutableSet<Player> by state {

    override fun add(element: Player): Boolean {
        setAndSaveGameMode(element)
        if (isNMS()) sendPlayerClient(element) else setPlayerBukkit(element)
        spawnDisplayElements(listOf(element))
        return state.add(element)
    }

    override fun addAll(elements: Collection<Player>): Boolean {
        elements.forEach { setAndSaveGameMode(it) }
        if (isNMS()) elements.forEach { sendPlayerClient(it) } else elements.forEach { setPlayerBukkit(it) }
        spawnDisplayElements(elements.toList())
        return state.addAll(elements)
    }

    override fun remove(element: Player): Boolean {
        resetGameMode(element)
        if (isNMS()) removePlayerClient(element) else cameraPlayer.camera.removeCamera(listOf(element))
        removeDisplayElements(listOf(element))
        return state.remove(element)
    }

    override fun removeAll(elements: Collection<Player>): Boolean {
        elements.forEach { resetGameMode(it) }
        if (isNMS()) elements.forEach { removePlayerClient(it) } else cameraPlayer.camera.removeCamera(elements.toList())
        removeDisplayElements(elements.toList())
        return state.removeAll(elements)
    }

    override fun clear() {
        state.forEach {
            resetGameMode(it)
        }
        removeDisplayElements(state.toList())
        if (isNMS()) state.forEach { removePlayerClient(it) } else cameraPlayer.camera.removeCamera(state.toList())
        state.clear()
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): PlayerList = this
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: PlayerList) {
        state = value
    }

    private fun spawnDisplayElements(player: List<Player>) {
        cameraPlayer.aliveDisplayEntities.forEach {
            it.value.spawn(player, cameraPlayer.akariEntity!!.entity, it.value.vector, it.key)
        }
    }

    private fun removeDisplayElements(player: List<Player>) {
        cameraPlayer.aliveDisplayEntities.forEach {
            NMSManager.nms.sendRemoveEntityPacket(it.value.akariEntity, player)
        }
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
        cameraPlayer.camera.kill(cameraPlayer.akariEntity!!.entity, listOf(player))
    }

    private fun sendPlayerClient(player: Player) {
        cameraPlayer.camera.setInterpolationDuration(cameraPlayer.akariEntity!!.entity, 1, listOf(player))
        cameraPlayer.camera.spawnForClient(cameraPlayer.akariEntity!!.entity, cameraPlayer.akariEntity?.serverEntity, player)
        cameraPlayer.camera.setCamera(cameraPlayer.akariEntity!!.entity, listOf(player))
    }


    private fun setPlayerBukkit(player: Player) {
        if (cameraPlayer.akariEntity == null) return
        cameraPlayer.camera.setCamera(cameraPlayer.akariEntity!!.entity, listOf(player))
    }

    private fun isNMS(): Boolean {
        if (cameraPlayer.akariEntity == null) return false
        if (cameraPlayer.camera !is NMSCamera) return false
        return true
    }
}