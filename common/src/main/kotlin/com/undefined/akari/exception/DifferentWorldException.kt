package com.undefined.akari.exception

import org.bukkit.World

class DifferentWorldException(
    val mainWorld: World,
    val wrongWorld: World
) : RuntimeException("This Location you are trying to add is in a different world. (${wrongWorld.name}) The correct world is (${mainWorld.name}))")