package com.hawk.nacos.demo.springboot.rest;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.hawk.nacos.demo.springboot.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Hawk
 */
@RestController
@RequestMapping("/demo")
public class CommonController {

    @Autowired
    private CommonService commonService;

    /**
     * 更新所有unionid
     */
    @PutMapping("/updateAllQQUnionid")
    public String updateAllQQUnionid() {
        commonService.updateAllQQUnionid();
        return "成功！";
    }

    @PostMapping("/generateUpdateSql")
    public String generateUpdateSql() {
        commonService.generateUpdateSql();
        return "成功！";
    }

    /**
     * boot也支持yml文件配置，需要在dataId中写完整
     */
    @NacosValue(value = "${useLocalCache:false}", autoRefreshed = true)
    private boolean useLocalCache;

    @NacosValue(value = "${test.skipLogin:false}", autoRefreshed = true)
    private boolean skipLogin;

    @NacosValue(value = "${test.loginUrl:'测试'}", autoRefreshed = true)
    private String loginUrl;

    @Value("${server.port}")
    private int serverPort;

    /**
     * nacos动态配置
     *
     * @return
     */
    @GetMapping("/config")
    public String config() {
        return "serverPort:" + serverPort + ";useLocalCache:" + useLocalCache + ";skipLogin:" + skipLogin + ";loginUrl:" + loginUrl;
    }

    @NacosInjected
    private NamingService namingService;

    @GetMapping("/discovery")
    public List<Instance> discovery(@RequestParam String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }

}
