package org.csu.chinese_chess_server.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

/**
 * TCP服务器配置类
 *
 * @author zjx
 * @since 2021/6/14 上午10:54
 */
@Configuration
//@PropertySource(value = "config.properties")
public class NetConfig {

    @Value("${tcp.server.ip}")
    public String SERVER_IP;

    @Value("${tcp.server.port}")
    public int SERVER_PORT;
}
