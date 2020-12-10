package com.hawk.nacos.demo.springcloudprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * nacos服务provider
 * 配置文件dataId支持自动拼接，默认文件后缀为properties
 * dataId 的完整格式如下:${prefix}-${spring.profiles.active}.${file-extension}
 * prefix 默认为 spring.application.name 的值
 * 当 spring.profiles.active 为空时，对应的连接符 - 也将不存在，dataId 的拼接格式变成 ${prefix}.${file-extension}
 * 详情见：https://nacos.io/zh-cn/docs/quick-start-spring-cloud.html
 * @author Hawk
 * @date 2020/12/10
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SpringcloudProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudProviderApplication.class, args);
    }

}
