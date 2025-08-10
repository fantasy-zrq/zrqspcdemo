package com.example.nettydemo.netty.n1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author zrq
 * @time 2025/8/8 17:09
 * @description
 */
@Slf4j
public class Demo {
    public static void main(String[] args) {
        EventLoopGroup group = new DefaultEventLoopGroup(2);
        new ServerBootstrap()
                .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast("handler1", new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        ByteBuf buf = (ByteBuf) msg;
                                        log.info(buf.toString(StandardCharsets.UTF_8));
                                        log.info("客户端地址：{}", ctx.channel().remoteAddress());
                                        ctx.fireChannelRead(msg);
                                    }

                                    @Override
                                    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                                        log.info("服务端已经回复消息：{}", "你好，客户端~");
                                        ctx.channel().writeAndFlush("你好，客户端~".getBytes(StandardCharsets.UTF_8));
                                        super.channelReadComplete(ctx);
                                    }

                                    @Override
                                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                        log.info("服务端出现异常--->{}", cause.getMessage());
                                        super.exceptionCaught(ctx, cause);
                                    }
                                })
                                .addLast(group, "handler2", new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        ByteBuf buf = (ByteBuf) msg;
                                        log.info(buf.toString(StandardCharsets.UTF_8));
                                        super.channelRead(ctx, msg);
                                    }
                                });
                    }
                })
                .bind(8080);
    }
}
