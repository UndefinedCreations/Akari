package com.undefined.akari.events

import com.undefined.akari.player.CameraPlayer
import org.bukkit.event.Cancellable

class PlayerCameraKickEvent(
    cameraPlayer: CameraPlayer
) : AkariEvent(), Cancellable {
    private var cancelled = false

    override fun isCancelled(): Boolean = cancelled

    override fun setCancelled(p0: Boolean) {
        this.cancelled = p0
    }
}