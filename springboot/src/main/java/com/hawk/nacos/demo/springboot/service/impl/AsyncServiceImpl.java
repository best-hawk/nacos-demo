package com.hawk.nacos.demo.springboot.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.hawk.nacos.demo.springboot.domian.TempUserDo;
import com.hawk.nacos.demo.springboot.repository.TempUserRepository;
import com.hawk.nacos.demo.springboot.service.AsyncService;
import com.hawk.nacos.demo.springboot.util.CompleteUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;

/**
 * 线程池接口
 *
 * @author Hawk
 * @date 2020/12/2
 */
@Service
@Slf4j
public class AsyncServiceImpl implements AsyncService {

    @Autowired
    private TempUserRepository tempUserRepository;

    @Autowired
    private RestTemplate restTemplate;

    @NacosValue(value = "${clientId:''}", autoRefreshed = true)
    private String clientId;

    @Override
    @Async("asyncServiceExecutor")
    public void updateUnionId(TempUserDo tempUser, String taskId) {
        try {
            if (StringUtils.isEmpty(clientId)) {
                throw new RuntimeException("clientId未配置");
            }
            String url = "https://graph.qq.com/oauth2.0/get_unionid?client_id=" + clientId + "&openid=%s&fmt=json";
            String resJson = restTemplate.getForEntity(String.format(url, tempUser.getOpenid()), String.class).getBody();
            JSONObject res = JSONObject.parseObject(resJson);
            if (StringUtils.isEmpty(res.getString("error")) && !StringUtils.isEmpty(res.getString("unionid"))) {
                tempUser.setUnionid(res.getString("unionid"));
                tempUserRepository.save(tempUser);
            }
        } catch (RestClientException e) {
            log.error(Thread.currentThread().getName() +
                            "执行失败！id:" + tempUser.getId() +
                            "；异常信息：" + e.getCause().toString()
//                            "异常信息：" + getExceptionDetail(e)
            );
        } finally {
            // 打印完成度
            CompleteUtil.showTaskCompleted(taskId);
        }
    }

    /**
     * 获取异常详情
     *
     * @param ex
     * @return
     */
    public static String getExceptionDetail(Exception ex) {
        String ret = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream pout = new PrintStream(out);
            ex.printStackTrace(pout);
            ret = new String(out.toByteArray());
            pout.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
