package com.boy0000.xray

import com.boy0000.xray.XrayHelpers.isXraying
import com.boy0000.xray.XrayHelpers.toBlockPos
import com.mineinabyss.geary.papermc.tracking.blocks.gearyBlocks
import com.mineinabyss.geary.prefabs.PrefabKey
import com.mineinabyss.idofront.commands.arguments.*
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.execution.IdofrontCommandExecutor
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import com.mineinabyss.idofront.messaging.broadcast
import com.mineinabyss.idofront.messaging.error
import com.mineinabyss.idofront.messaging.success
import com.mineinabyss.idofront.plugin.Plugins
import io.th0rgal.oraxen.api.OraxenBlocks
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import sendGameTestAddMarker
import kotlin.time.Duration
import kotlin.time.DurationUnit

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
            "popup" {
                val id by stringArg()
                val radius: Int? by intArg()
                val duration: Duration? by genericArg(parseFunction = Duration::parseOrNull)
                val message: String by stringArg { default = "" }
                playerAction {
                    val blockData = Material.matchMaterial(id)?.createBlockData() ?: (if (Plugins.isEnabled("Oraxen")) OraxenBlocks.getOraxenBlockData(id) else null) ?: (if (Plugins.isEnabled("Geary")) PrefabKey.ofOrNull(id)?.let { gearyBlocks.createBlockData(it) } else null) ?: return@playerAction sender.error("Invalid block id")
                    val radiusRange = (radius ?: xray.config.radius).let { -it..it }
                    for (x in radiusRange) for (y in radiusRange) for (z in radiusRange) {
                        val block = player.world.getBlockAt(player.location.clone().add(x.toDouble(), y.toDouble(), z.toDouble()))
                        if (block.type.isAir || blockData != block.blockData) continue
                        val default = xray.config.xrayBlocks.firstOrNull { it.block == block.blockData } ?: XrayConfig.XrayBlock.MinecraftXrayBlock(blockData.material)
                        player.sendGameTestAddMarker(block.toBlockPos(), default.color, message, duration?.toInt(DurationUnit.MILLISECONDS) ?: default.duration)
                    }
                    sender.success("Xray-Popup sent")
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
        return if (command.name == "xray") when (args.size) {
            1 -> listOf("popup", "debug", "reload", "test").filter { it.startsWith(args[0], ignoreCase = true)}
            2 -> when (args[0]) {
                "popup", "test" -> Material.entries.map { it.name }.filter { it.startsWith(args[1], ignoreCase = true)}
                else -> emptyList()
            }
            3 -> when (args[0]) {
                "popup" -> listOf("<radius>")
                else -> emptyList()
            }
            4 -> when (args[0]) {
                "popup" -> listOf("<duration>")
                else -> emptyList()
            }
            5 -> when (args[0]) {
                "popup" -> listOf("<message>")
                else -> emptyList()
            }
            else -> emptyList()
        } else emptyList()
    }
}