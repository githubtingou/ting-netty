package com.ting.netty.nio;

import java.nio.channels.Selector;

/**
 * @author lishuang
 * @version 1.0
 * @date 2021/8/20
 */
public class MySelector {
    public static void main(String[] args) throws Exception {
        // 得到一个Selector选择器
        Selector open = Selector.open();
    }
}
