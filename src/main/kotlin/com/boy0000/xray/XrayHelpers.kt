package com.boy0000.xray

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player

object XrayHelpers {

    fun nearbyBlocks(player: Player): Set<Block> {
        return mutableSetOf<Block>().apply {
            for (x in -64..64) for (y in -64..64) for (z in -64..64) {
                val block = player.world.getBlockAt(player.location.clone().add(x.toDouble(), y.toDouble(), z.toDouble()))
                if (block.type == Material.DIAMOND_BLOCK || block.type == Material.DIAMOND_ORE) add(block)
            }
        }
    }
}