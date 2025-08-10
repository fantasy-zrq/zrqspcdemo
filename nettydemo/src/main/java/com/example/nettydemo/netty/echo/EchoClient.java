package com.example.nettydemo.netty.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * @author zrq
 * @time 2025/8/10 18:40
 * @description
 */
@Slf4j
public class EchoClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ChannelFuture channelFuture = new Bootstrap()
                    .group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                            new Thread(() -> {
                                                Scanner scanner = new Scanner(System.in);
                                                while (true) {
                                                    String s = scanner.nextLine();
                                                    if ("q".equals(s)) {
                                                        ctx.close();
                                                        break;
                                                    }
                                                    ByteBuf buffer = ctx.alloc().buffer();
                                                    //客户端传一个buf
                                                    ctx.channel().writeAndFlush(buffer.writeByte(Byte.parseByte(s)));
                                                }
                                            }, "hh").start();
                                        }

                                        @Override
                                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                            ByteBuf buf = (ByteBuf) msg;
                                            log.debug("收到服务端的echo-->{}", buf.readByte());
                                            super.channelRead(ctx, buf);
                                        }
                                    });
                        }
                    })
                    .connect("localhost", 8080)
                    .sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            worker.shutdownGracefully();
        }
    }
}
