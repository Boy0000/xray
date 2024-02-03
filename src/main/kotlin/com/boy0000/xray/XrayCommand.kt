package com.boy0000.xray

import com.boy0000.xray.XrayHelpers.isXraying
import com.boy0000.xray.XrayHelpers.toBlockPos
import com.mineinabyss.idofront.commands.arguments.enumArg
import com.mineinabyss.idofront.commands.arguments.optionArg
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.execution.IdofrontCommandExecutor
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import com.mineinabyss.idofront.messaging.error
import com.mineinabyss.idofront.messaging.success
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import sendGameTestAddMarker

class XrayCommand : IdofrontCommandExecutor(), TabCompleter {

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
            "test" {
                val blockMaterial by enumArg<Material>()
                playerAction {
                    for (x in -5..5) for (y in -5..5) for (z in -5..5) {
                        val block = player.world.getBlockAt(player.location.clone().add(x.toDouble(), y.toDouble(), z.toDouble()))
                        if (block.type != blockMaterial) continue
                        xray.config.xrayBlocks.firstOrNull { it.block == block.blockData }
                            ?.let { player.sendGameTestAddMarker(block.toBlockPos(), it) }
                    }
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

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String> {
        return if (command.name == "xray") {
            if (args.size == 1) listOf("debug", "reload", "test").filter { it.startsWith(args[0], ignoreCase = true)}
            if (args.size == 2 && args[0] == "test") Material.entries.map { it.name }.filter { it.startsWith(args[1], ignoreCase = true)}
            else emptyList()
        } else emptyList()
    }
}