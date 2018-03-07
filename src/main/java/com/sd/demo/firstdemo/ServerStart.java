package com.sd.demo.firstdemo;

import com.sd.demo.codec.UserDecoder;
import com.sd.demo.codec.UserEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.Test;

/**
 * Created by sunda on 17-2-22.
 */
public class ServerStart {
    private int port = 9007;

    private EventLoopGroup bossGroup   = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    @Test
    public void startServer() {
        ServerBootstrap b = new ServerBootstrap();
        final ServerMsgCounterHandler serverMsgCounterHandler = new ServerMsgCounterHandler();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)
                            throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new UserDecoder());
                        pipeline.addLast(new UserEncoder());
                        pipeline.addLast(new ServerHandler());
                        pipeline.addLast(serverMsgCounterHandler);
                    }
                });
        try {
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void stopServer() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public void setPort(int port) {
        this.port = port;
    }
}
