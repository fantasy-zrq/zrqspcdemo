package com.example.nettydemo.netty.n1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author zrq
 * @time 2025/8/8 16:22
 * @description
 */
@Slf4j
public class NettyServer {
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new StringDecoder())
                                .addLast(new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        log.debug("time:{}", System.currentTimeMillis());
                                        log.debug("msg = {}", msg);
                                        List<Integer> list = List.of(1, 5, 6, 9, 7, 7, 45);
                                        super.channelRead(ctx, list);
                                    }
                                })
                                .addLast(new ChannelOutboundHandlerAdapter() {
                                    @Override
                                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                        log.debug("写出的handler1");
//                                        super.write(ctx, msg, promise);
                                        ctx.writeAndFlush("handler1");
                                    }
                                })
                                .addLast(new ChannelOutboundHandlerAdapter() {
                                    @Override
                                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                        log.debug("写出的handler2");
                                        super.write(ctx, msg, promise);
                                    }
                                })
                                .addLast(new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        List<Integer> list = (List<Integer>) msg;
                                        System.out.println("list = " + list);
                                        super.channelRead(ctx, msg);
                                    }
                                });

                    }
                })
                .bind(8080);
    }
}
