package com.boy0000.xray

import com.boy0000.xray.XrayHelpers.isXraying
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.execution.IdofrontCommandExecutor
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import com.mineinabyss.idofront.messaging.error
import com.mineinabyss.idofront.messaging.success

class XrayCommand : IdofrontCommandExecutor() {

    override val commands: CommandDSLEntrypoint = commands(xray.plugin) {
        "xray" {
            "debug" {
                playerAction {
                    if (XrayHelpers.debugPlayers.remove(player).not())
                        return@playerAction player.error("Debug mode is now disabled")

                    player.success("Debug mode is now enabled")
                    XrayHelpers.debugPlayers.add(player)
                }
            }
            "reload" {
                action {
                    xray.plugin.createXrayContext()
                    sender.success("Reloaded Xray config")
                }
            }
            playerAction {
                val toggle = player.isXraying?.not() ?: true
                player.isXraying = toggle
                if (!toggle) return@playerAction player.error("Xray is now disabled")
                player.success("Xray is now enabled")
                XrayHelpers.xrayNearbyBlocks(player)
            }
        }
    }
}