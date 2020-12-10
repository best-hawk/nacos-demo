package com.hawk.nacos.demo.springcloudconsumer.config;

import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 全局配置：调用其他微服务，一律使用基于Nacos权重的负载均衡算法
 * 同样适用于feign
 *
 * @author Hawk
 * @date 2020/12/10
 */
@Configuration
public class RibbonConfiguration {

    @Bean
    public IRule ribbonRule() {
        return new NacosWeightLoadBalancerRule();
    }

}
