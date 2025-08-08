package com.example.nettydemo.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author zrq
 * @time 2025/8/7 19:59
 * @description
 */
public class NIODemo {
    private final static String PATH = "C:\\Users\\zrq\\Desktop\\netty.txt";

    public static void main1(String[] args) throws FileNotFoundException {

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{97, 56, 12, 47, 32, 66});

        buffer.flip();
        System.out.println(buffer.get());
        System.out.println(buffer.get());
        buffer.mark();
        System.out.println(buffer.get());
        System.out.println(buffer.get());
        System.out.println(buffer.get());
        buffer.reset();
        System.out.println(buffer.get());
        System.out.println(buffer.get());
        System.out.println(buffer.get());
        System.out.println(buffer.get());
    }

    public static void main2(String[] args) {
        try (FileChannel channel = new RandomAccessFile(PATH, "r").getChannel()) {
            ByteBuffer buffer1 = ByteBuffer.allocate(3);
            ByteBuffer buffer2 = ByteBuffer.allocate(3);
            ByteBuffer buffer3 = ByteBuffer.allocate(5);
            channel.read(new ByteBuffer[]{buffer1, buffer2, buffer3});
            buffer1.flip();
            buffer2.flip();
            buffer3.flip();
            System.out.println(buffer1.get());
            System.out.println(buffer1.get());
            System.out.println(buffer1.get());
            System.out.println(buffer2.get());
            System.out.println(buffer2.get());
            System.out.println(buffer2.get());
            System.out.println(buffer3.get());
            System.out.println(buffer3.get());
            System.out.println(buffer3.get());
            System.out.println(buffer3.get());
            System.out.println(buffer3.get());
        } catch (IOException e) {
        }
    }

    public static void main(String[] args) {
        try (FileChannel channel = new RandomAccessFile(PATH, "rw").getChannel()) {
            ByteBuffer buffer1 = StandardCharsets.UTF_8.encode("zrq");
            ByteBuffer buffer2 = StandardCharsets.UTF_8.encode("ljc");
            ByteBuffer buffer3 = StandardCharsets.UTF_8.encode("你好");
            channel.write(new ByteBuffer[]{buffer1, buffer2, buffer3});
        } catch (IOException e) {
        }
    }
}
