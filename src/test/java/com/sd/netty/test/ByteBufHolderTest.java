package com.sd.netty.test;

import io.netty.buffer.*;
import org.junit.Test;

import java.util.stream.IntStream;

/**
 * Created by sunda on 17-6-8.
 */
public class ByteBufHolderTest {

    @Test
    public  void test(){
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer(200);
        IntStream.range(0,200).forEach(i->{
            byteBuf.writeInt(i);
        });

        String s = ByteBufUtil.hexDump(byteBuf);
        DefaultByteBufHolder byteBufHolder = new DefaultByteBufHolder(byteBuf);
        ByteBufHolder duplicate = byteBufHolder.duplicate();
        ByteBufHolder copy = byteBufHolder.copy();
        boolean equals = ByteBufUtil.equals(duplicate.content(), copy.content());

        System.out.println(equals);
        System.out.println(duplicate.equals(copy));
        System.out.println(s);
    }
}
