package com.example.nettydemo.bossworker;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zrq
 * @time 2025/8/8 14:53
 * @description
 */
public class ServerChanel {
    private static final Logger log = LoggerFactory.getLogger(ServerChanel.class);

    /**
     * boss线程只进行建立连接
     */
    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("boss");
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(8080));
        ssc.configureBlocking(false);
        Selector boss = Selector.open();
        ssc.register(boss, SelectionKey.OP_ACCEPT);
        Worker[] workers = new Worker[2];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker("worker-" + i);
        }
        AtomicInteger integer = new AtomicInteger();
        while (true) {
            //等待事件触发
            boss.select();
            Iterator<SelectionKey> iter = boss.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isAcceptable()) {
                    //通过连接关系取出这个ssc
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    //触发accept事件,返回的SocketChannel理解为拿到和客户端的通信管道
                    log.info("连接建立之前----");
                    SocketChannel sc = channel.accept();
                    //轮询
                    workers[integer.getAndIncrement() % workers.length].register(sc);
                    log.info("连接已建立----");
                    sc.configureBlocking(false);
                }
            }
        }
    }

    /**
     * Worker线程只监控读写事件
     */
    @Data
    static class Worker implements Runnable {

        private String name;
        private Selector selector;
        private Thread thread;
        private volatile Boolean start = false;
        private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();

        public Worker(String name) {
            this.name = name;
        }

        public void register(SocketChannel sc) throws IOException {
            if (!start) {
                thread = new Thread(this, name);
                selector = Selector.open();
                thread.start();
                start = true;
            }
            //这里为了保证sc.register(selector, SelectionKey.OP_READ);这步操作在worker线程中执行
            //采用队列的方式来解耦，实现在worker线程中注册这个sc
            queue.add(() -> {
                try {
                    sc.register(selector, SelectionKey.OP_READ);
                } catch (ClosedChannelException e) {
                    throw new RuntimeException(e);
                }
            });
            selector.wakeup();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    selector.select();
                    Runnable task = queue.poll();
                    if (task != null) {
                        task.run();
                    }
                    Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        iter.remove();
                        if (key.isReadable()) {
                            SocketChannel sc = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            sc.read(buffer);
                            buffer.flip();
                            System.out.println(buffer);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
