package com.example.nettydemo.rpc;

import com.example.nettydemo.rpc.data.Person;
import com.example.nettydemo.rpc.data.ReqStruct;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zrq
 * @time 2025/8/11 10:17
 * @description
 */
@Slf4j
public class RPCClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            new Bootstrap()
                    .group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline()
                                    // OUTBOUND: 发送数据时，将Java对象序列化为字节流
                                    .addLast(new ObjectEncoder())
                                    // INBOUND: 接收数据时，将字节流反序列化为Java对象
                                    .addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)))
                                    
                                    .addLast(new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                            String fullClassName = "com.example.nettydemo.rpc.data.UserServiceImpl";
                                            ReqStruct req = new ReqStruct(fullClassName, "sel"
                                                    , new Class[]{String.class}, new Object[]{"zrq"});
                                            log.debug("已经向服务端rpc请求数据");
                                            ctx.writeAndFlush(req);
                                            super.channelActive(ctx);
                                        }

                                        @Override
                                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                            Person person = (Person) msg;
                                            log.debug("接收到Rpc调用的返回数据---{}", person);
                                            super.channelRead(ctx, msg);
                                        }
                                    });
                        }
                    })
                    .connect("localhost", 9876)
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();
        } finally {
            worker.shutdownGracefully();
        }
    }
}
