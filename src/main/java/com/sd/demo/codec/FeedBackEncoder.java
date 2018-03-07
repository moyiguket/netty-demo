package com.sd.demo.codec;

import com.sd.demo.domain.FeedBack;
import com.sd.demo.domain.MessageConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;

/**
 * Created by sunda on 17-6-13.
 */
public class FeedBackEncoder extends MessageToByteEncoder<FeedBack> {

    @Override
    protected void encode(ChannelHandlerContext ctx, FeedBack msg, ByteBuf out) throws Exception {

        /**
         *
         */
        int bodyLen = 8+4+msg.getTitle().length()+4+msg.getContent().length() ;

        out.writeInt(MessageConstants.MAGIC_NUM);
        out.writeShort(3);
        out.writeInt(bodyLen);


        out.writeLong(msg.getFeedId());
        out.writeInt(msg.getTitle().length());
        /**
         * more efficient than write bytes
         */
        out.writeCharSequence(msg.getTitle(),Charset.forName("utf-8"));
        out.writeInt(msg.getContent().length());
        out.writeCharSequence(msg.getContent(),Charset.forName("utf-8"));



    }
}
