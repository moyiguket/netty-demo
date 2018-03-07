package com.sd.demo.codec;

import com.sd.demo.domain.MessageConstants;
import com.sd.demo.domain.User;
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
public class UserDecoder extends ByteToMessageDecoder{

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
                        int id = in.readInt();
                        /**
                         * more efficient than read(byte[])
                         */
                        int nameLen = in.readInt();
                        CharSequence charSequence = in.readCharSequence(nameLen, Charset.defaultCharset());
                        User user = new User(id, charSequence.toString());
                        out.add(user);
                    }
                } else {
                    in.resetReaderIndex();
                }
            }

        }
    }
}
