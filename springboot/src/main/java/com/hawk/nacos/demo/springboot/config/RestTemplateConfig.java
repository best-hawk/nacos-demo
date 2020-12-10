package com.hawk.nacos.demo.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author Hawk
 */
@Configuration
public class RestTemplateConfig {

    /**
     * #@LoadBalanced让这个RestTemplate在请求时拥有客户端负载均衡的能力，否则多线程爆炸调用时会抛出异常
     *
     * @param factory
     * @return
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 等待时间设置为30秒
        factory.setConnectTimeout(60 * 1000);
        factory.setReadTimeout(5 * 1000);
        return factory;
    }

}
