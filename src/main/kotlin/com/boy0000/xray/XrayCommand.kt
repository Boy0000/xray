package com.boy0000.xray

import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.execution.IdofrontCommandExecutor
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import net.minecraft.core.BlockPos
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import sendGameTestAddMarker
import java.awt.Color

class XrayCommand : IdofrontCommandExecutor() {

    override val commands: CommandDSLEntrypoint = commands(xray) {
        "xray" {
            playerAction {
                xray.toggledXray.add(player.uniqueId) || xray.toggledXray.remove(player.uniqueId)
                player.sendMessage("Xray toggled: ${player.uniqueId in xray.toggledXray}")
                if (player.uniqueId in xray.toggledXray) for (block in XrayHelpers.nearbyBlocks(player)) {
                    val craftPlayer = player as? CraftPlayer ?: return@playerAction
                    val serverPlayer = craftPlayer.handle
                    val pos = BlockPos(block.x, block.y, block.z)
                    serverPlayer.sendGameTestAddMarker(pos, "debug/game_test_add_marker", Color.GREEN.rgb, 0xFFFFFF)
                    player.sendMessage("Xraying ${block.location}")
                }
            }
        }
    }
}