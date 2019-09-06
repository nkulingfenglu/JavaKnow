package Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @program: NettyStudyFirst
 * @description: Server
 * @author: jiuchai
 * @create: 2019-09-05 09:40
 */
public class TimeServer {

    public static void main(String[] args) {
        int port = 9898;
        new TimeServer().bind(port);
    }

    /**
     * 服务端连接相关
     *
     * @param port
     */
    public void bind(int port) {
        //配置nio的线程组 其实就是两个线程池
        //bossGroup用于处理连接请求和建立连接 当作注线程池
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //workGroup用于处理IO请求 当做从线程池
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            //设置服务端的启动类
            //netty服务端的应用开发的主入口
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024).
                    childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new TimeServerHandler());
                        }
                    });
            //用bind绑定端口 调用同步方法 等待绑定的完成
            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println(Thread.currentThread().getName() + "  服务端开始监听端口，等待客户端连接... ");
            //等待服务器连接关闭后 main方法退出 程序结束
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
