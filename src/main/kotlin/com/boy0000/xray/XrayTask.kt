package com.boy0000.xray

import com.github.shynixn.mccoroutine.bukkit.launch
import kotlinx.coroutines.delay
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.phys.Vec3
import org.bukkit.Bukkit
import java.util.*
import kotlin.time.Duration.Companion.seconds

object XrayTask {

    /*fun startXrayTask() {
        xray.launch {
            while (true) {
                delay(1.seconds)

                for (uuid in xray.toggledXray) {
                    val player = Bukkit.getPlayer(uuid) ?: continue

                    for (block in XrayHelpers.nearbyBlocks(player)) {
                        val (entityPacket, metadataPacket) = xray.xrayCache.getOrPut(block.location) {
                            val entityId = Entity.nextEntityId()
                            XrayPlugin.XrayPackets(
                                ClientboundAddEntityPacket(
                                    entityId, UUID.randomUUID(),
                                    block.location.x, block.location.y, block.location.z,
                                    0f, 0f, EntityType.BLOCK_DISPLAY,
                                    0, Vec3.ZERO, 0.0
                                ),
                                ClientboundSetEntityDataPacket(
                                    entityId, listOf(
                                        SynchedEntityData.DataValue(23, EntityDataSerializers.INT, 1),
                                    )
                                )
                            )
                        }
                        (player as CraftPlayer).handle.connection.send(entityPacket)
                        player.handle.connection.send(metadataPacket)
                        player.sendMessage("Xraying ${block.location}")
                    }
                }
            }
        }
    }*/
}