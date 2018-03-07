package com.sd.demo.advanceddemo;

import com.sd.demo.codec.FeedBackEncoder;
import com.sd.demo.codec.UserDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.junit.Test;

/**
 * Created by sunda on 17-2-22.
 */
public class ServerStart {
    private int port = 8887;

    private EventLoopGroup commonGroup = new EpollEventLoopGroup(4);

    private int sndbuf = 256;
    private int recbuf = 256;
    private int KB_BYTES = 1024;

    private int lowwartermark = 8;
    private int highwartermark = 32;

    @Test
    public void startServer() {
        ServerBootstrap b = new ServerBootstrap();
        final ServerMsgCounterHandler serverMsgCounterHandler = new ServerMsgCounterHandler();

        EventExecutorGroup eventExecutors = new DefaultEventExecutorGroup(Runtime.getRuntime().availableProcessors());
        b.group(commonGroup)
                .channel(EpollServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.SO_SNDBUF, sndbuf * KB_BYTES)
                .option(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(lowwartermark*KB_BYTES, highwartermark*KB_BYTES))
                .childOption(ChannelOption.SO_RCVBUF, recbuf * KB_BYTES)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)
                            throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("msgDecoder",new UserDecoder());
                        pipeline.addLast("msgEncoder",new FeedBackEncoder());
                        pipeline.addLast(eventExecutors,"serverhandler",new ServerHandler());
                        pipeline.addLast(eventExecutors,"msgCounter",serverMsgCounterHandler);
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
        commonGroup.shutdownGracefully();
    }

    public void setPort(int port) {
        this.port = port;
    }
}
