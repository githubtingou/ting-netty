package com.ting.netty.nio.chat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * 聊天服务端
 *
 * @author lishuang
 * @version 1.0
 * @date 2021/8/24
 */
public class NioChatServer {

    private Selector selector;
    private ServerSocketChannel listenChannel;
    private final static int PORT = 6667;

    public NioChatServer() {
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));

            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 监听
     */
    public void listen() {

        try {
            while (true) {
                int count = selector.select();
                if (count > 0) {
                    // 有时间处理
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {

                        SelectionKey key = iterator.next();

                        if (key.isAcceptable()) {
                            SocketChannel accept = listenChannel.accept();
                            accept.configureBlocking(false);
                            // 将accept注册到selector上
                            accept.register(selector, SelectionKey.OP_READ);
                            System.out.println("有客户端连接到服务端" + accept.getRemoteAddress());
                        }
                        //发生read事件
                        if (key.isReadable()) {
                            readData(key);

                        }
                        // 防止重读，进行删除
                        iterator.remove();

                    }

                } else {
                    System.out.println("等待。。。。");
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取客户端信息
     */
    private void readData(SelectionKey key) {
        // 定义一个SocketChannel
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);

            if (count > 0) {
                // 输出缓存区的数据
                String msg = new String(buffer.array()).trim();
                System.out.println("输出客户端发来的数据：" + msg);

                // 向其他客户端转发消息(去掉自己)
                sendInfoToOtherClient(msg, channel);

            }

        } catch (Exception e) {
            try {
                System.out.println(channel.getRemoteAddress() + "离线。。。");

                //取消注册
                key.cancel();
                // 关闭通道
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


    }

    /**
     * 向其他客户端转发消息(去掉自己)
     *
     * @param msg
     * @param channel
     */
    private void sendInfoToOtherClient(String msg, SocketChannel channel) {
        System.out.println("向其他服务器发送数据....");
        // 遍历所有注册到Selectot上的socketChannel 并排除自身neg
        for (SelectionKey key : selector.keys()) {
            Channel targetChannel = key.channel();
           // 排除自己
            if (targetChannel instanceof SocketChannel && targetChannel !=channel){

                SocketChannel dest = (SocketChannel) targetChannel;
                // 将msg写入buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8));
                try {
                    System.out.println("--------------");
                    dest.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) {
        NioChatServer chatServer = new NioChatServer();
        chatServer.listen();
    }

}
