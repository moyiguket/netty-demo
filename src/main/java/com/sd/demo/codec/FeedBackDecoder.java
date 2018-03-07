package com.sd.demo.codec;


import com.sd.demo.domain.FeedBack;
import com.sd.demo.domain.MessageConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by sunda on 17-6-13.
 *
 * MUST_NOT Sharable
 */
public class FeedBackDecoder extends ByteToMessageDecoder{

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        /**
         * 先读取魔术字,判断是否是需要处理的消息
         */
        if (in.readableBytes() > 4){
            int magicNum = in.readInt();
            /**
             * 回退点,如果后续包不完整,等待传输
             */
            in.markReaderIndex();
            if (magicNum == MessageConstants.PING){
                ctx.channel().writeAndFlush(MessageConstants.PONG);
                return;
            } else if (magicNum == MessageConstants.PONG) {
                ctx.channel().writeAndFlush(MessageConstants.PING);
                return;
            }
            if (magicNum == MessageConstants.MAGIC_NUM){
                if (in.readableBytes() > 6) {
                    short version = in.readShort();
                    int bodyLen = in.readInt();
                    if (in.readableBytes() >= bodyLen){
                        /**
                         * 获取到body
                         */

                        long feedId = in.readLong();
                        int titleLen = in.readInt();

                        /**
                         * more efficient than read(byte[])
                         */
                        CharSequence title = in.readCharSequence(titleLen, Charset.defaultCharset());
                        int contentLen = in.readInt();
                        CharSequence content = in.readCharSequence(contentLen, Charset.defaultCharset());
                        FeedBack feedBack = new FeedBack();
                        feedBack.setFeedId(feedId);
                        feedBack.setTitle(title.toString());
                        feedBack.setContent(content.toString());
                        out.add(feedBack);
                    }
                } else {
                    in.resetReaderIndex();
                }
            }

        }
    }
}
