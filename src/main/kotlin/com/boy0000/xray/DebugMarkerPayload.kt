import io.netty.buffer.Unpooled
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket
import net.minecraft.network.protocol.common.custom.GameEventListenerDebugPayload
import net.minecraft.network.protocol.common.custom.GameTestAddMarkerDebugPayload
import net.minecraft.network.protocol.game.DebugPackets
import net.minecraft.server.level.ServerPlayer

fun ServerPlayer.sendGameTestAddMarker(pos: BlockPos, message: String, color: Int, duration: Int) {
    val packet: Packet<*> = ClientboundCustomPayloadPacket(GameTestAddMarkerDebugPayload(pos, color, message, duration))
    connection.send(packet)
}