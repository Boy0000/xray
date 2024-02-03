package com.boy0000.xray

import com.mineinabyss.idofront.messaging.broadcast
import net.minecraft.core.BlockPos
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import sendGameTestAddMarker
import sendGameTestClearAllMarkers

object XrayHelpers {

    val debugPlayers = mutableSetOf<Player>()
    private val XRAY_ENABLED = NamespacedKey.fromString("xray_enabled", xray.plugin)!!
    var Player.isXraying
        get() = persistentDataContainer.get(XRAY_ENABLED, PersistentDataType.BOOLEAN)
        set(value) = persistentDataContainer.set(XRAY_ENABLED, PersistentDataType.BOOLEAN, value ?: true)

    fun Block.toBlockPos() = BlockPos(x, y, z)

    fun xrayNearbyBlocks(player: Player) {
        //player.sendGameTestClearAllMarkers()
        for (x in -64..64) for (y in -64..64) for (z in -64..64) {
            val block = player.world.getBlockAt(player.location.clone().add(x.toDouble(), y.toDouble(), z.toDouble()))
            xray.config.xrayBlocks.firstOrNull { it.block == block.blockData }?.let {
                if (player in debugPlayers) broadcast("${player.name} is xraying at ${block.toBlockPos()}")
                player.sendGameTestAddMarker(block.toBlockPos(), it)
            }
        }
    }
}

infix fun BlockData.isIn(xrayBlocks: List<XrayConfig.XrayBlock>) = xrayBlocks.any { it.block == this }
