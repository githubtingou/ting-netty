package com.ting.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * nio 服务端
 *
 * @author lishuang
 * @version 1.0
 * @date 2021/8/23
 */
public class NioServer {
    public static void main(String[] args) throws Exception {
        // 创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 创建selector
        Selector selector = Selector.open();

        // 绑定端口
        serverSocketChannel
                .socket()
                .bind(new InetSocketAddress(8888));

        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 把serverSocketChannel绑定到selector,事件为SelectionKey.OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


        while (true) {
            if (selector.select(10000) == 0) {
                // 没有事件时等待了1s
                System.out.println("服务器等待了10s，无连接");
                continue;
            }
            // 获取selectionKey集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                // 获取各个事件
                SelectionKey key = keyIterator.next();

                if (key.isAcceptable()) {
                    System.out.println("有客户端连接");
                    // 生成一个socketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();

                    socketChannel.configureBlocking(false);

                    // 关联一个buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                }

                if (key.isReadable()) {
                    System.out.println("读取_________");
                    SocketChannel channel = (SocketChannel) key.channel();
                    // 获取buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println("从客户端获取数据" + new String(buffer.array()));
                }

                System.out.println(key.channel());
                // 手动从集合中移除当前的selectKey.防止重复操作
                keyIterator.remove();
            }

        }

    }
}
