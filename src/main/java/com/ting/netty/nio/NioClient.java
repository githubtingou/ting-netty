package com.ting.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * nio客户端
 *
 * @author lishuang
 * @version 1.0
 * @date 2021/8/23
 */
public class NioClient {
    public static void main(String[] args) throws Exception {
        // 创建channel
        SocketChannel socketChannel = SocketChannel.open();
        // 设置为非阻塞
        socketChannel.configureBlocking(false);

        // 注册到服务端
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8888);

        if (!socketChannel.connect(address)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("客户端连接服务端需要事件，客户端不会阻塞，可以做其他工作");
            }
        }

        String string = "nio-ting---";
        ByteBuffer buffer = ByteBuffer.wrap(string.getBytes(StandardCharsets.UTF_8));
        // 发送数据
        socketChannel.write(buffer);
        System.in.read();

    }
}
