package Client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.logging.Logger;

/**
 * @program: NettyStudyFirst
 * @description: ClientHandler
 * @author: jiuchai
 * @create: 2019-09-05 15:44
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = Logger.getLogger(TimeClientHandler.class.getName());
    /**
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String body = new String(bytes,"UTF-8");
        System.out.println(Thread.currentThread().getName() + " 服务端发力的消息是：" + body);
        log.info("addhadada");
        ctx.close();

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String repMsg = "client send msg" + Thread.currentThread().getName();
        byte[] msgBytes = repMsg.getBytes("UTF-8");
        ByteBuf byteBuf = Unpooled.buffer(msgBytes.length);
        //将原数组的数据传输到缓冲区
        byteBuf.writeBytes(msgBytes);
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warning("Unexpected exception from downstream ");
        ctx.close();
    }
}
