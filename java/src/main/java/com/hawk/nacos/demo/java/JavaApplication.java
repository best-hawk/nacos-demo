package com.hawk.nacos.demo.java;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

/**
 * @author Hawk
 */
@SpringBootApplication
public class JavaApplication {

    public static void main(String[] args) {
        try {
            // Initialize the configuration service, and the console automatically obtains the following parameters through the sample code.
            String serverAddr = "http://hawk.com:8808/nacos";
            Properties properties = new Properties();
            properties.put("serverAddr", serverAddr);

            // if need username and password to login
            properties.put("username","nacos");
            properties.put("password","nacos");

            ConfigService configService = NacosFactory.createConfigService(properties);
            System.out.println("1111111");
        } catch (NacosException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



}
