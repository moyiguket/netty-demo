package com.sd.demo.codec;

import com.sd.demo.domain.MessageConstants;
import com.sd.demo.domain.User;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;

/**
 * Created by sunda on 17-6-13.
 */
public class UserEncoder extends MessageToByteEncoder<User> {

    @Override
    protected void encode(ChannelHandlerContext ctx, User msg, ByteBuf out) throws Exception {

        /**
         *
         */
        int bodyLen = 4 + 4+ msg.getName().length();

        out.writeInt(MessageConstants.MAGIC_NUM);
        out.writeShort(3);
        out.writeInt(bodyLen);

        out.writeInt(msg.getId());

        /**
         * more efficient than write bytes
         */
        out.writeInt(msg.getName().length());
        out.writeCharSequence(msg.getName(),Charset.forName("utf-8"));



    }
}
