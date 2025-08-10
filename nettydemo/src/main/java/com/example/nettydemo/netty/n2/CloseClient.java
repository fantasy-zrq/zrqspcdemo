package com.example.nettydemo.netty.n2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zrq
 * @time 2025/8/10 14:00
 * @description
 */
@Slf4j
public class CloseClient {
    public static void main(String[] args) throws InterruptedException {
        ChannelFuture future = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder())
                                .addLast(new MyHandler());
                    }
                })
                .connect("localhost", 8080);
        //通过同步阻塞main线程的方式来获取已经建立连接成功的channel
        Channel channel = future.sync().channel();

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        AtomicInteger integer = new AtomicInteger();
        executor.schedule(() -> {
            if (integer.get() == 5) {
                //在这里关闭channel，和main不是一个线程
                channel.close();
                return;
            }
            log.debug("hello!--{}", integer.getAndIncrement());
        }, 1L, TimeUnit.SECONDS);

        //获取关闭channel的Future对象
        ChannelFuture closeFuture = channel.closeFuture();
        //通过同步阻塞main线程的方式来等待NioEventLoopGroup中的线程关闭channel，等到关闭，阻塞结束。
        closeFuture.sync();
        log.debug("通道关闭");
    }

    static class MyHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            log.debug("channel被激活");
            ctx.channel().writeAndFlush("你好---------------");
            super.channelActive(ctx);
        }
    }
}
