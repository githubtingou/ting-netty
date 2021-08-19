package com.ting.netty.netty.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * netty当spring boot启动后启动
 *
 * @author lishuang
 * @version 1.0
 * @date 2021/8/17
 */
@Component
@Slf4j
public class NettyStart implements ApplicationListener<ApplicationStartedEvent> {

    @Value(value = "netty.port:1234")
    private Integer nettyPort;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("spring boot 已启动完成，开始加载netty...");
        // 启动netty
        try {
            new NettyServer(nettyPort).start();
        } catch (InterruptedException e) {
            log.error("netty启动失败", e);
        }
        log.info("netty启动成功,端口号[{}]", nettyPort);

    }
}
