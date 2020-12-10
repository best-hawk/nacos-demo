package com.hawk.nacos.demo.springcloudprovider.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Hawk
 * @date 2020/12/9
 */
@RestController
@RequestMapping("/common")
@RefreshScope
public class CommonController {

    @Value("${useLocalCache:false}")
    private boolean useLocalCache;

    @Value(value = "${test.skipLogin:false}")
    private boolean skipLogin;

    @Value(value = "${test.loginUrl:'测试'}")
    private String loginUrl;

    @Value("${server.port}")
    private int serverPort;

    @GetMapping("/config")
    public String get() {
        return "serverPort:" + serverPort + ";useLocalCache:" + useLocalCache + ";skipLogin:" + skipLogin + ";loginUrl:" + loginUrl;
    }

}
