package com.hawk.nacos.demo.springboot.service;

import com.hawk.nacos.demo.springboot.domian.TempUserDo;

/**
 * @author Hawk
 */
public interface AsyncService {

    /**
     * 执行异步任务：更新unionid
     *
     * @param tempUser
     * @param taskId 完成工具任务id
     */
    void updateUnionId(TempUserDo tempUser, String taskId);

}
