package com.ting.netty.netty.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.time.LocalDate;

/**
 * netty服务
 *
 * @author lishuang
 * @version 1.0
 * @date 2021/8/16
 */
public class NettyServer {
    /**
     * 端口
     */
    private final int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup loopGroup = new NioEventLoopGroup();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .group(group, loopGroup) // 绑定线程池
                    .channel(NioServerSocketChannel.class) // 指定使用的channel
                    .localAddress(port) // 绑定监听的端口
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 绑定客户端连接时的操作
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            System.out.println("收到新的连接，时间：" + LocalDate.now());

                            // 因为webSocket是基于http协议的，所有这边需要使用http解编码器
                            ch.pipeline().addLast(new HttpServerCodec());

                            //以块的方式来写的处理器
                            ch.pipeline().addLast(new ChunkedWriteHandler());
                            ch.pipeline().addLast(new HttpObjectAggregator(8191));
                            ch.pipeline().addLast(new MyWebSocketHandler());
                            ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws",
                                    null,
                                    true,
                                    65536 * 10));


                        }

                    });
            // 服务器异步创建绑定
            ChannelFuture sync = bootstrap.bind().sync();
            System.out.println(NettyServer.class + "启动正在监听" + sync.channel().localAddress());
            // 关闭服务器通道
            sync.channel().closeFuture().sync();
        } finally {
            // 释放线程池资源
            System.out.println("释放线程池资源");
            group.shutdownGracefully().sync();
            loopGroup.shutdownGracefully().sync();

        }

    }
}
