package dto;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

/**
 * Command 编解码器
 *
 * @author zjx
 * @since 2021/6/15 下午9:02
 */
public class CommandEncoder extends ByteToMessageCodec<Command> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Command command, ByteBuf out) throws Exception {
        byte[] bytes = JSON.toJSONBytes(command);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf == null) return;

        if (byteBuf.readableBytes() <= 4) return;
        byteBuf.markReaderIndex();
        int len = byteBuf.readInt();
        if (len < 0) {
            channelHandlerContext.close();
        }

        if (byteBuf.readableBytes() < len) {
            byteBuf.resetReaderIndex();
            return;
        }

        byte[] data = new byte[len];
        byteBuf.readBytes(data);
        Command command = JSON.parseObject(data, Command.class);
        list.add(command);
    }
}
