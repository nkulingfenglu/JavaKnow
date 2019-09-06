package Client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @program: NettyStudyFirst
 * @description: TimeClient
 * @author: jiuchai
 * @create: 2019-09-05 15:20
 */
public class TimeClient {

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new Thread(new MyThread()).start();
        }
    }

    static class MyThread implements Runnable{

        public void run() {
            connect("10.134.148.233",9898);
        }
        public void connect(String host , int port){
            //配置线程池组
            EventLoopGroup group = new NioEventLoopGroup();
            try{
                //创建客户端启动类，并进行配置
                Bootstrap bootstrap = new Bootstrap();
                //注意下面放的channel设置为NioSocketChannel 与服务端的配置不同
                bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY,true).
                        handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                socketChannel.pipeline().addLast(new TimeClientHandler());
                            }
                        });
                //发起异步连接请求
                ChannelFuture future = bootstrap.connect(host,port).sync();
                System.out.println(Thread.currentThread().getName() + "  客户端发起一步连接请求...");
                future.channel().closeFuture().sync();

            }catch(Exception e){
                e.printStackTrace();
            }finally {
                group.shutdownGracefully();
            }

        }
    }

}
