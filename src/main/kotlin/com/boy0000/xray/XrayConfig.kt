@file:OptIn(ExperimentalSerializationApi::class)

package com.boy0000.xray

import com.mineinabyss.geary.papermc.tracking.blocks.gearyBlocks
import com.mineinabyss.geary.prefabs.PrefabKey
import com.mineinabyss.geary.prefabs.serializers.PrefabKeySerializer
import com.mineinabyss.idofront.serialization.DurationSerializer
import com.mineinabyss.idofront.serialization.MaterialByNameSerializer
import com.mineinabyss.idofront.util.toColor
import io.th0rgal.oraxen.api.OraxenBlocks
import kotlinx.serialization.*
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

@Serializable
data class XrayConfig(
    val delay: @Serializable(DurationSerializer::class) Duration = 1.seconds,
    val radius: Int = 64,
    val xrayBlocks: List<XrayBlock> = listOf(
        XrayBlock.MinecraftXrayBlock(Material.DIAMOND_ORE),
        XrayBlock.GearyXrayBlock("mineinabyss:crate1"),
        XrayBlock.OraxenXrayBlock("amethyst_ore")
    )
) {

    @Serializable
    @Polymorphic
    sealed interface XrayBlock {
        @Transient val block: BlockData
        val color: Int
        val duration: Int
        val message: String

        @Serializable
        data class MinecraftXrayBlock(
            val material: @Serializable(MaterialByNameSerializer::class) Material,
            @SerialName("color") private val _color: String = "0x00FF00",
            @SerialName("duration") private val _duration: @Serializable(DurationSerializer::class) Duration = 1.seconds,
            @EncodeDefault(EncodeDefault.Mode.NEVER) override val message: String = material.name,
        ) : XrayBlock {
            @Transient override val block = material.takeIf { material.isBlock }?.createBlockData() ?: Material.AIR.createBlockData()
            @Transient override val duration: Int = _duration.toInt(DurationUnit.MILLISECONDS)
            @Transient override val color: Int = _color.toColor().asARGB()
        }

        @Serializable
        data class OraxenXrayBlock(
            val blockId: String,
            @SerialName("color") private val _color: String = "0x00FF00",
            @SerialName("duration") private val _duration: @Serializable(DurationSerializer::class) Duration = 1.seconds,
            @EncodeDefault(EncodeDefault.Mode.NEVER) override val message: String = blockId,
        ) : XrayBlock {
            @Transient override val block by lazy { OraxenBlocks.getOraxenBlockData(blockId) ?: Material.AIR.createBlockData() }
            @Transient override val duration: Int = _duration.toInt(DurationUnit.MILLISECONDS)
            @Transient override val color: Int = _color.toColor().asARGB()
        }

        @Serializable
        data class GearyXrayBlock(
            val blockId: String,
            @SerialName("color") private val _color: String = "0x00FF00",
            @SerialName("duration") private val _duration: @Serializable(DurationSerializer::class) Duration = 1.seconds,
            @EncodeDefault(EncodeDefault.Mode.NEVER) override val message: String = blockId,
        ) : XrayBlock {
            @Transient override val block by lazy { PrefabKey.ofOrNull(blockId)?.let { gearyBlocks.createBlockData(it) } ?: Material.AIR.createBlockData() }
            @Transient override val duration: Int = _duration.toInt(DurationUnit.MILLISECONDS)
            @Transient override val color: Int = _color.toColor().asARGB()
        }
    }
}

