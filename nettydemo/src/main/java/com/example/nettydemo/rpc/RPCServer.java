package com.example.nettydemo.rpc;

import com.example.nettydemo.rpc.data.Person;
import com.example.nettydemo.rpc.data.ReqStruct;
import com.example.nettydemo.rpc.data.UserServiceImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author zrq
 * @time 2025/8/11 10:16
 * @description 一个简单的额本地模拟的RPC框架
 * 需要给server端加上虚拟机参数，让高版本jdk兼容cglib
 * --add-opens java.base/java.lang=ALL-UNNAMED
 */
@Slf4j
public class RPCServer {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline()
                                    // INBOUND: 接收数据时，将字节流反序列化为Java对象
                                    .addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)))
                                    // OUTBOUND: 发送数据时，将Java对象序列化为字节流
                                    .addLast(new ObjectEncoder())
                                    .addLast(new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                            log.debug("接收到请求参数{}", msg);
                                            ReqStruct reqStruct = (ReqStruct) msg;
                                            String fullClassname = reqStruct.getClassName();
                                            Class<?> clz = Class.forName(fullClassname);
                                            Method method = clz.getDeclaredMethod(reqStruct.getMethodName(), reqStruct.getMethodParamTypes());
                                            UserServiceImpl proxy = getMyProxy(reqStruct.getClassName());
                                            Person result = (Person) method.invoke(proxy, reqStruct.getMethodParams());
                                            log.debug("结果={}", result);
                                            ctx.writeAndFlush(result);
                                            super.channelRead(ctx, msg);
                                        }
                                    });
                        }
                    })
                    .bind("localhost", 9876)
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    private static UserServiceImpl getMyProxy(String className) {
        MyCglibProxy myCglibProxy = new MyCglibProxy(new UserServiceImpl());
        return ((UserServiceImpl) myCglibProxy.getCglibProxyObject());
    }

    static class MyCglibProxy implements MethodInterceptor {

        private Object target;

        public MyCglibProxy(Object target) {
            this.target = target;
        }

        public Object getCglibProxyObject() {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(target.getClass());
            enhancer.setCallback(this);//回调当前实例的intercept方法
            return enhancer.create();
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            //产生代理对象
            return methodProxy.invokeSuper(o, objects);
        }
    }
}
