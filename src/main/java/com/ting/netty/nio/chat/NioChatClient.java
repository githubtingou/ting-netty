package com.ting.netty.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 聊天服务端
 *
 * @author lishuang
 * @version 1.0
 * @date 2021/8/24
 */
public class NioChatClient {

    private final static String host = "127.0.0.1";

    private final static int port = 6667;

    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    //初始化工作
    public NioChatClient() {
        try {
            selector = Selector.open();
            // 连接服务器
            socketChannel = socketChannel.open(new InetSocketAddress(host, port));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            username = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(username + " is ok...");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 发送数据
     *
     * @param info
     */
    private void sendInfo(String info) {
        info = username + "说：" + info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 读取从服务端回复的消息
    private void readInfo() {
        try {
            int read = selector.select();

            if (read > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        // 获取相关的通道
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        channel.read(buffer);
                        String msg = new String(buffer.array()).trim();
                        System.out.println(msg);

                    }
                    iterator.remove();

                }
            } else {
               //  System.out.println("没有可用的通道。。。。 ");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        NioChatClient chatClient = new NioChatClient();

        // 启动一个线程,读取从服务器发送的数据
        new Thread(() -> {
            while (true) {
                chatClient.readInfo();
                try {
                    Thread.currentThread().sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }).start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            chatClient.sendInfo(line);
        }


    }
}