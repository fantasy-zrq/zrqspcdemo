package com.example.nettydemo.netty.n2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zrq
 * @time 2025/8/10 16:43
 * @description
 */
@Slf4j
public class PoolTest {
    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        int i1 = buffer.writerIndex();
        System.out.println("i1 = " + i1);
        buffer.writeByte(1);
        int i2 = buffer.writerIndex();
        System.out.println("i2 = " + i2);
        buffer.writeInt(2);
        int i3 = buffer.writerIndex();
        System.out.println("i3 = " + i3);

        byte b = buffer.readByte();
        System.out.println("b = " + b);
        int i = buffer.readerIndex();
        System.out.println("i = " + i);
    }
}
