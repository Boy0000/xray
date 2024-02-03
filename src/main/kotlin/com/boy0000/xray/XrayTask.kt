package com.boy0000.xray

import com.github.shynixn.mccoroutine.bukkit.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

object XrayTask {

    fun startXrayTask() {
        runBlocking {
            xray.plugin.launch {
                while (true) {
                    xray.plugin.server.worlds.flatMap { it.players }.forEach(XrayHelpers::xrayNearbyBlocks)
                    delay(xray.config.delay)
                }
            }
        }
    }
}