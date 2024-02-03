package com.boy0000.xray

import net.minecraft.core.BlockPos
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import sendGameTestAddMarker

object XrayHelpers {

    val debugPlayers = mutableSetOf<Player>()
    private val XRAY_ENABLED = NamespacedKey.fromString("xray_enabled", xray.plugin)!!
    var Player.isXraying
        get() = persistentDataContainer.get(XRAY_ENABLED, PersistentDataType.BOOLEAN)
        set(value) = persistentDataContainer.set(XRAY_ENABLED, PersistentDataType.BOOLEAN, value ?: true)

    fun Block.toBlockPos() = BlockPos(x, y, z)

    fun xrayNearbyBlocks(player: Player) {
        val r = xray.config.radiusRange
        if (player.isXraying == true) for (x in r) for (y in r) for (z in r) {
            val block = player.world.getBlockAt(player.location.clone().add(x.toDouble(), y.toDouble(), z.toDouble()))
            if (block.type.isAir) continue
            xray.config.xrayBlocks.firstOrNull { it.block == block.blockData }?.let {
                player.sendGameTestAddMarker(block.toBlockPos(), it)
            }
        }
    }
}
