package com.example.nettydemo.netty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zrq
 * @time 2025/8/10 18:40
 * @description
 */
@Slf4j
public class EchoServer {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    //这个泛型写ChannelInitializer<NioSocketChannel>，而不是ChannelInitializer<NioServerSocketChannel>
                    //的原因是因为childHandler是服务于worker组的，处理的是与客户端的写入写出请求，所以通道要使用NioSocketChannel
                    //假如调用handler方法，就需要添加泛型为NioServerSocketChannel，来处理与客户端的连接请求。
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                            ByteBuf buf = (ByteBuf) msg;
                                            log.debug("InHandler1-接收到客户端传来的---{}", buf);
                                            super.channelRead(ctx, buf);
                                        }
                                    })
                                    .addLast(new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                            ByteBuf buf = (ByteBuf) msg;
                                            log.debug("InHandler2-接收到InHandler1传来的---{}", buf.getByte(0));
                                            //显示调用write方法写回，才能调用到ChannelOutboundHandlerAdapter出站方法
                                            ctx.writeAndFlush(buf.retain());
                                        }
                                    })
                                    .addLast(new ChannelOutboundHandlerAdapter() {
                                        @Override
                                        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                            log.debug("OutHandler1-接收到需要向客户端写的数据---{}", msg);
                                            super.write(ctx, msg, promise);
                                        }
                                    });
                        }
                    })
                    .bind(8080)
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
