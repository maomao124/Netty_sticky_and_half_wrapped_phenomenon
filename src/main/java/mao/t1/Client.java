package mao.t1;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * Project name(项目名称)：Netty_黏包与半包现象
 * Package(包名): mao.t1
 * Class(类名): Client
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2023/3/23
 * Time(创建时间)： 22:10
 * Version(版本): 1.0
 * Description(描述)： 客户端，演示粘包现象
 */

@Slf4j
public class Client
{
    public static void main(String[] args)
    {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try
        {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(worker);
            bootstrap.handler(new ChannelInitializer<SocketChannel>()
            {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception
                {
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter()
                    {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception
                        {
                            log.debug("sending...");
                            Random r = new Random();
                            char c = 'a';
                            for (int i = 0; i < 10; i++)
                            {
                                ByteBuf buffer = ctx.alloc().buffer();
                                buffer.writeBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
                                ctx.writeAndFlush(buffer);
                            }
                        }
                    });
                }
            });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8080).sync();
            channelFuture.channel().closeFuture().sync();

        }
        catch (InterruptedException e)
        {
            log.error("client error", e);
        }
        finally
        {
            worker.shutdownGracefully();
        }
    }

}
