package com.sd.demo.advanceddemo;

import com.sd.demo.codec.FeedBackDecoder;
import com.sd.demo.codec.UserEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by sunda on 17-2-22.
 */
public class ClientStart {
    private int sndbuf = 256;
    private int recbuf = 256;
    private int KB_BYTES = 1024;

    private int lowwartermark = 8;
    private int highwartermark = 32;

    private int port = 8887;
    private String host = System.getProperty("host","127.0.0.1");

    private EventLoopGroup workerGroup = new EpollEventLoopGroup();

    @Test
    public void startClient(){

        Bootstrap b = new Bootstrap();
        b.group(workerGroup)
                .channel(EpollSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_SNDBUF, sndbuf * KB_BYTES)
                .option(ChannelOption.SO_RCVBUF, recbuf * KB_BYTES)
                .option(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(lowwartermark*KB_BYTES, highwartermark*KB_BYTES))

                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)
                            throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new IdleStateHandler(60,60,120, TimeUnit.SECONDS));
                        pipeline.addLast(new FeedBackDecoder());
                        pipeline.addLast(new UserEncoder());
                        pipeline.addLast(new ClientHandler());
                    }
                });
        try {
            ChannelFuture sync = b.connect(host, port).sync();

            sync.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void stopServer() {
        workerGroup.shutdownGracefully();
    }

    public void setPort(int port) {
        this.port = port;
    }


}
