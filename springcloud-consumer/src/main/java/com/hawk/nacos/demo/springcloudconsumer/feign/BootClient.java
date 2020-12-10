package com.hawk.nacos.demo.springcloudconsumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 调用boot客户端feign
 *
 * @author Hawk
 * @date 2020/12/10
 */
@FeignClient("nacos-demo-springboot")
public interface BootClient {

    /**
     * 查询配置
     *
     * @return
     */
    @GetMapping("/demo/config")
    String getConfig();

}
