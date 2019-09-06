package Server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @program: NettyStudyFirst
 * @description: ServlerHandle 用于对IO请求进行处理
 * @author: jiuchai
 * @create: 2019-09-05 10:58
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 收到客户端消息 自动触发。
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //将msg转化为netty的bytebuf类型的字节序列（数组）
        ByteBuf buf = (ByteBuf) msg;
        //获取缓冲区的可读区域字节数组 然后创建一个新的byte数组
        byte[] byt = new byte[buf.readableBytes()];
        buf.readBytes(byt);
        String body = new String(byt, "UTF-8");
        System.out.println(Thread.currentThread().getName() + "  服务端收到的消息为 ：" + body);

        //回复消息
        String replyMessage = "i am server , 消息接受成功！";
        //开辟一个新的缓冲区
        ByteBuf byteBuf = Unpooled.copiedBuffer(replyMessage.getBytes());
        //异步发送给客户端
        ctx.write(byteBuf);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //flush:将消息队列中的消息写入到SocketChannel中发送给对方
        //netty的write方法不会讲消息直接写入SocketChannel中，而是将消息放入到发送缓存数组中，等待flush将消息写入socketchannel
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}

