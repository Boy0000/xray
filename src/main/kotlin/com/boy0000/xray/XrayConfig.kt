package com.boy0000.xray

import com.mineinabyss.idofront.serialization.DurationSerializer
import com.mineinabyss.idofront.util.toColor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.bukkit.Material
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Serializable
data class XrayConfig(
    val delay: @Serializable(DurationSerializer::class) Duration = 1.seconds,
    val xrayBlocks: List<XrayBlock> = listOf(XrayBlock())
) {
    @Serializable
    data class XrayBlock(
        @SerialName("block") private val _block: String = "DIAMOND_ORE",
        @SerialName("color") private val _color: String = "0x00FF00",
        val duration: @Serializable(DurationSerializer::class) Duration = 1.seconds,
    ) {
        @Transient val block = (Material.matchMaterial(_block) ?: Material.DIAMOND_ORE).createBlockData() //TODO Add support for Blocky & Oraxen
        val message: String = block.material.name
        @Transient val color = _color.toColor()
    }
}