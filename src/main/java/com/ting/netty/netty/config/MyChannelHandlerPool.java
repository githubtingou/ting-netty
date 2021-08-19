package com.ting.netty.netty.config;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 通道组池，管理所有websocket连接
 *
 * @author lishuang
 * @version 1.0
 * @date 2021/8/16
 */
public class MyChannelHandlerPool {

    public MyChannelHandlerPool(){}

    public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

}
