package com.sd.demo.advanceddemo;

import com.sd.demo.domain.FeedBack;
import com.sd.demo.domain.User;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.internal.ThreadLocalRandom;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.JdkLoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * Created by sunda on 17-2-22.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private InternalLogger logger = JdkLoggerFactory.getInstance(MethodHandles.lookup().lookupClass());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        if(msg != null && msg instanceof User){
            logger.info("server received msg "+msg.toString());

            FeedBack feedBack = new FeedBack();
            long feedId = ThreadLocalRandom.current().nextLong();
            feedBack.setFeedId(feedId);
            feedBack.setTitle("title---"+feedId);
            feedBack.setContent("content---------"+feedId);
            //channel.write() is not effective,case it will go through the whole pipeline. use ctx.write() instead.
            //flush is a system call ,do not call too much
            //use voidPromise to reduce unnecessary object creation
            ctx.channel().writeAndFlush(feedBack,ctx.voidPromise());

            //This one is more efficient,use it
            ctx.write(feedBack,ctx.voidPromise());
            ctx.channel().eventLoop().submit(() -> {
                ctx.flush();
            });
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
