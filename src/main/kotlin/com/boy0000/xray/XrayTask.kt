package com.boy0000.xray

import com.boy0000.xray.XrayHelpers.isXraying
import com.github.shynixn.mccoroutine.bukkit.launch
import com.mineinabyss.idofront.messaging.broadcastVal
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import sendGameTestClearAllMarkers
import kotlin.time.Duration.Companion.seconds

object XrayTask {

    fun startXrayTask() {
        runBlocking {
            xray.plugin.launch {
                while (true) {
                    delay(1.seconds)

                    xray.plugin.server.worlds.forEach { world ->
                        world.players.forEach player@{ player ->
                            player.isXraying.broadcastVal(player.name)
                            when (player.isXraying) {
                                true -> XrayHelpers.xrayNearbyBlocks(player)
                                //false -> player.sendGameTestClearAllMarkers()
                                //null -> return@player
                                else -> return@player

                            }
                        }
                    }
                }
            }
        }
    }
}