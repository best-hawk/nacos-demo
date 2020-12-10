package com.hawk.nacos.demo.springboot.service.impl;

import com.hawk.nacos.demo.springboot.domian.TempUserDo;
import com.hawk.nacos.demo.springboot.repository.TempUserRepository;
import com.hawk.nacos.demo.springboot.service.AsyncService;
import com.hawk.nacos.demo.springboot.service.CommonService;
import com.hawk.nacos.demo.springboot.util.CompleteUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Hawk
 * @date 2020/12/2
 */
@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

    @Autowired
    private TempUserRepository tempUserRepository;

    @Autowired
    private AsyncService asyncService;

    private String id;

    /**
     * 方法层添加@Transactional后，事务改为一次性提交，否则单次提交
     * #@transactional(rollbackFor = Exception.class)
     */
    @Override
    public void updateAllQQUnionid() {
        // 查询没有unionid数据集合
//        List<TempUserDo> tempUserList = tempUserRepository.findLackUnionIdList();
        List<TempUserDo> tempUserList = tempUserRepository.findUnionIdList();
        // 新建完成度任务，获取任务id
        String taskId = CompleteUtil.newInstance(tempUserList.size());
        for (TempUserDo tempUser : tempUserList) {
            if (null != tempUser && !StringUtils.isEmpty(tempUser.getOpenid())) {
                asyncService.updateUnionId(tempUser, taskId);
            }
        }
    }

    @Override
    public void generateUpdateSql() {
        String updateSql = "UPDATE `honeycomb_user`.`crm_login_type` SET `union_id` = '%s' WHERE `Account_unique` = '%s';\n";
        List<TempUserDo> tempUserList = tempUserRepository.findUnionIdList();

        //创建指定文件
        File file = new File("D://update.sql");

        try {
            if (!file.exists()) {
                // 如果指定文件不存在，新建文件
                file.createNewFile();
            }
            // 创建FileWriter对象
            FileWriter fw = new FileWriter(file);
            for (TempUserDo tempUser : tempUserList) {
                if (null != tempUser && !StringUtils.isEmpty(tempUser.getOpenid())) {
                    // 向文件写入数据
                    fw.write(String.format(updateSql, tempUser.getUnionid(), tempUser.getOpenid()));
                }
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
