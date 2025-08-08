package com.example.nettydemo.socketchanel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author zrq
 * @time 2025/8/7 21:56
 * @description
 */
public class ClientChanel {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress(8080));
        ByteBuffer buffer = StandardCharsets.UTF_8.encode("hello,server!!");
        sc.write(buffer);
        System.out.println("qq");
    }
}
