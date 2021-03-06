package com.ting.netty.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * 文件channel
 *
 * @author lishuang
 * @version 1.0
 * @date 2021/8/19
 */
public class MyFileChannel {
    public static void main(String[] args) throws Exception {
        // 写入文件
        String str = "ting";
        try (FileOutputStream stream = new FileOutputStream("D:\\ting.txt")) {
            // 获取Channel
            FileChannel channel = stream.getChannel();

            // 创建一个buffer
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            byteBuffer.put(str.getBytes(StandardCharsets.UTF_8));

            // 写--》读
            byteBuffer.flip();

            // 将buffer写入channel
            channel.write(byteBuffer);
        }

        File file = new File("D:\\ting.txt");
        // 读取文件
        try (FileInputStream inputStream = new FileInputStream(file)) {
            FileChannel channel = inputStream.getChannel();

            ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
            channel.read(buffer);

            System.out.println(new String(buffer.array()));
        }

        // 使用一个buffer进行操作

        try (FileInputStream inputStream = new FileInputStream("D:\\ting.txt");
             FileOutputStream outputStream = new FileOutputStream("D:\\ting1.txt")) {

            // 读文件
            FileChannel channel = inputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(str.length());

            while (true) {
                // 重置数据，防止上次的操作影响到本次的执行
                buffer.clear();

                int read = channel.read(buffer);
                if (read == -1) {
                    break;
                }
            }

            // 读--》写
            buffer.flip();

            // 写入文件
            FileChannel outputStreamChannel = outputStream.getChannel();
            outputStreamChannel.write(buffer);
        }
        
    }
}
