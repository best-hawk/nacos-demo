package com.hawk.nacos.demo.springboot;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 1.springboot接入nacos测试
 * 1)@NacosPropertySource dataId必须和nacos配置中心保持一致，不支持自动拼接后缀
 * 2)自动注册服务
 * 2.更新qq unionid（邮件申请qq互联临时接口）
 * 1)注解线程池使用，性能测试
 * 2)IO读写测试
 * 3)jpa 原生sql查询
 * 4)jpa条件查询测试
 * 5)计时计量控件
 * 6)大流量请求时，代码块加锁
 * 7)BigDecimal小数点控制
 * 8)递归转化时间单位
 * 9)将数据缓存到静态变量中，并定时清除
 * 3.slf4j日志分类打印
 *
 * @author Hawk
 * @date 2020/12/1
 */
@SpringBootApplication
@NacosPropertySource(dataId = "nacos-demo-springboot.yml", groupId = "SPRING_BOOT", autoRefreshed = true)
public class SpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }

    @NacosInjected
    private NamingService namingService;

    @Value("${server.port}")
    private int serverPort;

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 在服务启动后向Nacos服务注册
     *
     * @throws NacosException
     */
    @PostConstruct
    public void registerInstance() throws NacosException {
        try {
            // 以本机ip，服务名，端口号向nacos注册服务
            namingService.registerInstance(applicationName, InetAddress.getLocalHost().getHostAddress(), serverPort);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

}
