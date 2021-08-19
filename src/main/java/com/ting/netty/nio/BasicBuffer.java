package com.ting.netty.nio;

import java.nio.IntBuffer;

/**
 * nio buffer
 *
 * @author lishuang
 * @version 1.0
 * @date 2021/8/18
 */
public class BasicBuffer {

    public static void main(String[] args) {
        // 创建一个长度为5的intBuffer
        IntBuffer intBuffer = IntBuffer.allocate(5);
        // 存
        for (int i = 0; i < 5; i++) {
            intBuffer.put(i);

        }
        // 读
        // 读写切换,必要
        intBuffer.flip();
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }

    }
}
