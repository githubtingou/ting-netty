package com.ting.netty.netty.config;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义处理器
 *
 * @author lishuang
 * @version 1.0
 * @date 2021/8/16
 */
public class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * 建立连接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端建立连接，通道开启=" + ctx.channel().id());
        MyChannelHandlerPool.group.add(ctx.channel());
    }

    /**
     * 断开连接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端断开连接，通道关闭=" + ctx.channel().id());
        MyChannelHandlerPool.group.remove(ctx.channel());
    }


    /**
     * 获取客户端发送的消息
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 首次连接的是 FullHttpRequest
        if (!ObjectUtils.isEmpty(msg) && msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();
            Map<String, String> urlParam = getUrlParam(uri);
            System.out.println("解析uri后的参数为" + JSON.toJSON(urlParam));
            if (uri.contains("?")) {
                String newUrl = uri.substring(0, uri.indexOf("?"));
                System.out.println("新地址:" + newUrl);
                request.setUri(newUrl);
            }
        } else if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame socketFrame = (TextWebSocketFrame) msg;
            System.out.println("客户端收到服务端数据：" + socketFrame.text());
            for (int i = 0; i < 100; i++) {
                sendAllMessage(socketFrame.text() + i);


            }
//            sendAllMessage(socketFrame.text());

        }
        super.channelRead(ctx, msg);
    }

    /**
     * 收到消息后群发消息
     *
     * @param text 消息
     */
    private void sendAllMessage(String text) {

        MyChannelHandlerPool.group.writeAndFlush(new TextWebSocketFrame("服务端发送------》" + text));
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

    }

    /**
     * 解析uri地址上拼接的参数
     *
     * @param uri uri
     * @return map
     */
    public static Map<String, String> getUrlParam(String uri) {
        Map<String, String> map = new HashMap<>();
        uri = uri.replace("?", ";");
        if (!uri.contains(";")) {
            return map;
        }
        if (uri.split(";").length > 0) {
            String[] arr = uri.split(";")[1].split("&");
            for (String s : arr) {
                String key = s.split("=")[0];
                String value = s.split("=")[1];
                map.put(key, value);
            }
            return map;
        }
        return map;

    }
}
