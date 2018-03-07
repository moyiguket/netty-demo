package com.sd.demo.firstdemo;

import com.sd.demo.codec.UserDecoder;
import com.sd.demo.codec.UserEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by sunda on 17-2-22.
 */
public class ClientStart {


    private int port = 9007;
    private String host = System.getProperty("host","127.0.0.1");

    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    @Test
    public void startClient(){

        Bootstrap b = new Bootstrap();
        b.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)
                            throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new IdleStateHandler(60,60,120, TimeUnit.SECONDS));
                        pipeline.addLast(new UserDecoder());
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
