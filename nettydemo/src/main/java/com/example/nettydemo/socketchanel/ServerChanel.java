package com.example.nettydemo.socketchanel;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author zrq
 * @time 2025/8/7 21:56
 * @description
 */
@Slf4j
public class ServerChanel {
    /**
     * 使用选择器selector来统一管理所有的chanel
     */
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(8080));
        //这个chanel的行为是accept,只要这个ssc调用了accept()方法，就代表这个accept事件被处理了
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            selector.select();
            //是遍历的selector.selectedKeys()集合，这个集合和selector维护的集合不一样
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                //这个集合迭代出一个变量，必须要删除这个变量，否则这个变量会重复消费
                //在selectedKeys集合中，消费过的变量，会将其关注事件删除，但变量不删除，所以会存在重复消费，但是没有事件。
                //报错空指针
                iterator.remove();
                log.info("key:{}", key);
                if (key.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = serverSocketChannel.accept();
                    sc.configureBlocking(false);
                    //这里设置的是chanel的行为--》从chanel中读数据
                    sc.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    try {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        if (channel.read(buffer) == -1) {
                            //结果==-1代表客户端那边正常结束断开
                            key.cancel();
                        } else {
                            buffer.flip();
                            System.out.println(buffer);
                        }
                    } catch (IOException e) {
                        key.cancel();
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
