package com.sd.demo.advanceddemo;

import com.sd.demo.domain.MessageConstants;
import com.sd.demo.domain.User;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.JdkLoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Created by sunda on 17-2-22.
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private InternalLogger logger = JdkLoggerFactory.getInstance(MethodHandles.lookup().lookupClass());
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

        /**
         * eventLoop is a ScheduledExecutorService.
         */
        ctx.channel().eventLoop().scheduleAtFixedRate(() -> {
            /**
             * FastThreadLocal is faster than threadLocal ,use it.
             * do not create StringBuilder every time, re use it.
             */
            StringBuilder builder = InternalThreadLocalMap.get().stringBuilder();

            /**
             * more efficient than new Random(),use it
             */
            int userId = ThreadLocalRandom.current().nextInt(100);
            builder.append("userName");
            builder.append(userId);
            String userName = builder.toString();
            User user = new User(userId, userName);
//             ctx.channel()
//                    .writeAndFlush(user, ctx.voidPromise());

            //check writable ,do not write too fast.
            if (ctx.channel().isWritable()) {
                ctx.channel().writeAndFlush(user,ctx.voidPromise());
            }else {
                logger.error("too fast ,abort this message ",user);
            }
        },1,1, TimeUnit.MILLISECONDS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        logger.info("client received msg "+msg.toString());

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if(IdleStateEvent.class.isAssignableFrom(evt.getClass())){
            IdleStateEvent event = (IdleStateEvent)evt;
            switch (event.state()) {
                case ALL_IDLE:
                    System.out.println("all idle");
                    break;
                case READER_IDLE:
                    System.out.println("reader idle");
                    ctx.channel().writeAndFlush(MessageConstants.PING);
                    break;
                case WRITER_IDLE:
                    System.out.println("write idle");
                    break;
                default:
                    break;
            }
        }
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
