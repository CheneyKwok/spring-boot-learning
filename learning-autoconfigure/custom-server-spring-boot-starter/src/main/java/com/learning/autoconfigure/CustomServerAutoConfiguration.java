package com.learning.autoconfigure;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetSocketAddress;


@Configuration
@EnableConfigurationProperties(ServerProperties.class) // 使 ServerProperties 配置属性类生效
public class CustomServerAutoConfiguration {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    @ConditionalOnClass(HttpServer.class)// 表示需存在 com.sun.net.httpserver.HttpServer 类。该类为 JDK 自带
    public HttpServer httpServer(ServerProperties serverProperties) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(serverProperties.getPort()), 0);
        httpServer.start();
        logger.info("[HttpServer][启动服务器成功，端口: {}]", serverProperties.getPort());

        return httpServer;
    }
}
