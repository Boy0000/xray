package com.boy0000.xray

import com.boy0000.xray.XrayConfig.XrayBlock
import com.boy0000.xray.XrayConfig.XrayBlock.*
import com.mineinabyss.idofront.plugin.Plugins
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object XrayBlockSerializer : KSerializer<XrayBlock> {
    override val descriptor: SerialDescriptor = PolymorphicSerializer(XrayBlock::class).descriptor

    override fun serialize(encoder: Encoder, value: XrayBlock) {
        when (value) {
            is MinecraftXrayBlock -> MinecraftXrayBlock.serializer().serialize(encoder, value)
            is OraxenXrayBlock -> OraxenXrayBlock.serializer().serialize(encoder, value)
            is GearyXrayBlock -> GearyXrayBlock.serializer().serialize(encoder, value)
        }
    }

    override fun deserialize(decoder: Decoder): XrayBlock {
        val dec = decoder.beginStructure(descriptor)
        var index = dec.decodeElementIndex(descriptor)
        var result: XrayBlock? = null
        while (index != CompositeDecoder.DECODE_DONE) {
            result = when (index) {
                0 -> decoder.decodeSerializableValue(MinecraftXrayBlock.serializer())
                1 -> if (Plugins.isEnabled("Oraxen")) decoder.decodeSerializableValue(OraxenXrayBlock.serializer()) else null
                2 -> if (Plugins.isEnabled("Geary")) decoder.decodeSerializableValue(GearyXrayBlock.serializer()) else null
                else -> throw SerializationException("Unknown index $index")
            }
            index = dec.decodeElementIndex(descriptor)
        }
        dec.endStructure(descriptor)
        return result ?: throw SerializationException("No XrayBlock found")
    }


}