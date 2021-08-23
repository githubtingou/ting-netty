package com.ting.netty.nio;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author lishuang
 * @version 1.0
 * @date 2021/8/20
 */
public class MyMappedByteBuffer {

    public static void main(String[] args) throws Exception {
        // MappedByteBuffer 可以直接在内存（堆外内存）中修改，操作系统不需要拷贝一次
        ByteBuffer allocate = MappedByteBuffer.allocate(10);
        FileChannel channel;
        try (RandomAccessFile rw = new RandomAccessFile("D:\\ting.txt", "rw")) {
            channel = rw.getChannel();
             /*
                第一个参数：设置模式，如：读写、只读、
                第二个参数：可以修改的起始位置
                第三个参数：映射到内存的大小，即可以修改文件的字节数量
            */
            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

            // 根据下标修改文件的内容
            map.put(0, (byte) 'L');
            map.put(1, (byte) 's');
        }


    }


}
