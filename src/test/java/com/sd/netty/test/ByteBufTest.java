package com.sd.netty.test;

import io.netty.buffer.*;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * Created by da on 2017-12-22.
 */
public class ByteBufTest {

    @Test
    public void test(){
        int capacity = 1024;
        ByteBuf heapBuffer = UnpooledByteBufAllocator.DEFAULT.heapBuffer(capacity);
        int num = 11;
        String words = "ABCD";
        heapBuffer.writeInt(num);
        heapBuffer.writeCharSequence(words, Charset.defaultCharset());
        int readInt = heapBuffer.readInt();
        CharSequence charSequence = heapBuffer.readCharSequence(words.length(), Charset.defaultCharset());
    }
}
