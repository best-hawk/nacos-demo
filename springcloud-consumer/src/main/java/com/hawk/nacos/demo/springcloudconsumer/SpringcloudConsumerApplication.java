package com.hawk.nacos.demo.springcloudconsumer;

import com.hawk.nacos.demo.springcloudconsumer.feign.BootClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * nacos服务consumer
 *
 * @author Hawk
 * @date 2020/12/10
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class SpringcloudConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudConsumerApplication.class, args);
    }

    /**
     * restTemplate启用负载均衡
     *
     * @return
     */
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RestController
    @RequestMapping("/test")
    public class TestController {

        @Autowired
        private RestTemplate restTemplate;

        @Autowired
        private BootClient bootClient;

        /**
         * 使用默认ribbon调用provider服务
         *
         * @return
         */
        @GetMapping("/provider")
        public String provider() {
            return restTemplate.getForObject("http://nacos-demo-springcloud-provider/config", String.class);
        }

        /**
         * 使用默认ribbon调用boot服务
         *
         * @return
         */
        @GetMapping("/boot")
        public String boot() {
            return restTemplate.getForObject("http://nacos-demo-springboot/demo/config", String.class);
        }

        /**
         * 使用feign调用
         *
         * @return
         */
        @GetMapping("/feign/boot")
        public String feignBoot() {
            return bootClient.getConfig();
        }

    }

}
