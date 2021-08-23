package com.ting.netty.nio;

import org.springframework.util.ObjectUtils;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering（分散）:将数据写入到buffer时，可以采用buffer数组，依次写入
 * Gathering：如buffer读取数据,可以采用buffer数组，依次读
 *
 * @author lishuang
 * @version 1.0
 * @date 2021/8/20
 */
public class MyScatteringAndGathering {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(7777);

        // 绑定端口到socket，并启动
        serverSocketChannel.socket().bind(address);

        // 设置buffer数组
        ByteBuffer[] buffers = new ByteBuffer[2];
        buffers[0] = ByteBuffer.allocate(5);
        buffers[1] = ByteBuffer.allocate(2);

        // 等待客户端连接
        SocketChannel channel = serverSocketChannel.accept();

        // 设置接收字节的大小
        int messageSize = 7;

        while (true) {
            int byteSize = 0;
            while (byteSize < messageSize) {
                long read = channel.read(buffers);
                byteSize += read;
                System.out.println("现在读取的数量" + byteSize);
                System.out.println("位置" + byteSize);
                Arrays.stream(buffers).forEach(byteBuffer -> {
                    System.out.println("byteBuffer.position() = " + byteBuffer.position());
                    System.out.println("byteBuffer.limit() = " + byteBuffer.limit());
                });

                //将所有的buffer进行反转
                Arrays.stream(buffers).forEach(Buffer::flip);

                // 将数据读出，显示到客户端
                long byteWirte = 0;
                if (byteWirte < messageSize) {
                    channel.write(buffers);
                    byteWirte += 1;
                }

                // 将所有的buffer进行clear
                Arrays.stream(buffers).forEach(Buffer::clear);
                System.out.println("----结束----");
                System.out.println("byteWirte = " + byteWirte);
                System.out.println("read = " + read);
                System.out.println("messageSize = " + messageSize);
            }

        }

    }


}
