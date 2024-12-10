package com.undefined.akari.entity

import com.undefined.akari.objects.CamaraPoint
import org.bukkit.entity.Player

interface CamaraDisplay {

    fun spawn(cPoint: CamaraPoint, player: Player) = spawn(cPoint, mutableSetOf(player))

    fun spawn(cPoint: CamaraPoint, players: Collection<Player>)

    fun remove()

    fun teleport(cPoint: CamaraPoint)

    fun setViewer(player: Player)

    fun setViewers(players: List<Player>) = players.forEach { setViewer(it) }

    fun interpolationDuration(interpolatorDuration: Int)

}