package com.ting.netty.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * bio server
 *
 * @author lishuang
 * @version 1.0
 * @date 2021/8/18
 */
public class BIOServer {
    public static void main(String[] args) throws Exception {
        // 创建一个线程池
        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("bio服务启动");

        while (true) {
            // 监听，等待客户端连接
            Socket socket = serverSocket.accept();
            System.out.println("连接到客户端");
            newFixedThreadPool.execute(() -> {
                // 与客户端通讯
                handle(socket);
            });

        }

    }

    public static void handle(Socket socket) {
        System.out.println("handle线程id" + Thread.currentThread().getId()
                + "[名称]" + Thread.currentThread().getName());
        byte[] bytes = new byte[1024];
        try (InputStream stream = socket.getInputStream()) {
            // 这里用 while (true)的原因是因为如果不使用的话交互一次就会关闭连接
            while (true) {
                int read = stream.read(bytes);
                if (read != -1) {
                    System.out.println("输出客户端的数据:" + new String(bytes, 0, read));
                } else {
                    break;
                }
            }


        } catch (IOException e) {
            System.out.println("失败" + e);

        }
    }
}
