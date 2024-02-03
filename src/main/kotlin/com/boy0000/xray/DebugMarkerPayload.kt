import com.boy0000.xray.XrayConfig
import com.mineinabyss.idofront.time.inWholeTicks
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket
import net.minecraft.network.protocol.common.custom.GameTestAddMarkerDebugPayload
import net.minecraft.network.protocol.common.custom.GameTestClearMarkersDebugPayload
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import kotlin.time.DurationUnit

fun Player.sendGameTestAddMarker(pos: BlockPos, xrayBlock: XrayConfig.XrayBlock) {
    val duration = xrayBlock.duration.toInt(DurationUnit.MILLISECONDS)
    val debugPayload = GameTestAddMarkerDebugPayload(pos, xrayBlock.color.asRGB(), xrayBlock.message, duration)
    (this as CraftPlayer).handle.connection.send(ClientboundCustomPayloadPacket(debugPayload))
}

fun Player.sendGameTestClearMarker(pos: BlockPos) {
    val packet: Packet<*> = ClientboundCustomPayloadPacket(GameTestAddMarkerDebugPayload(pos, 0x000000, "", 0))
    (this as CraftPlayer).handle.connection.send(packet)
}

fun Player.sendGameTestClearAllMarkers() {
    val packet: Packet<*> = ClientboundCustomPayloadPacket(GameTestClearMarkersDebugPayload())
    (this as CraftPlayer).handle.connection.send(packet)
}