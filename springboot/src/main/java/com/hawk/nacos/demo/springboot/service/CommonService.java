package com.hawk.nacos.demo.springboot.service;

import lombok.Data;

/**
 * @author Hawk
 * @date 2020/12/2
 */
public interface CommonService {

    /**
     * 更新所有QQ用户的unionid
     */
    void updateAllQQUnionid() ;

    /**
     * 生成更新用户unionid sql语句
     */
    void generateUpdateSql();

}
