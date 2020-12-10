package com.hawk.nacos.demo.springboot.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 完成度组件
 *
 * @author Hawk
 * @date 2020/12/4
 */
@Slf4j
@Data
public class CompleteUtil {

    private static Map<String, TaskEntity> completeEntityMap = new HashMap<>();

    /**
     * 记录日志时间间隔，单位：毫秒
     */
    private static Integer LOG_TIME_INTERVAL = 1 * 1000;

    /**
     * 展示日志-完成比例%
     */
    private static Integer TIME_REMAIN_COMPLETE_PERCENT = 10;

    /**
     * 展示日志-已运行时间
     */
    private static Integer TIME_REMAIN_SECONDS_RUN = 15;

    /**
     * 新建任务
     *
     * @param maxNum
     * @return
     */
    public static String newInstance(int maxNum) {
        String taskId = UUID.randomUUID().toString().replace("-", "");
        TaskEntity entity = new TaskEntity();
        entity.setTaskId(taskId);
        long currentTimeMillis = System.currentTimeMillis();
        entity.setStartTime(currentTimeMillis);
        entity.setMaxNum(maxNum);
        entity.setCurrentNum(0);
        completeEntityMap.put(taskId, entity);
        // 初始化新线程池监控任务（使用线程池可走debug）
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.initialize();
        pool.execute(() -> {
            // 执行子线程，查看当前任务是否执行完毕（10秒一次）
            while (true) {
                if (completeEntityMap.get(taskId).getCurrentNum() == maxNum) {
                    // 执行完毕时删除该任务
                    completeEntityMap.remove(taskId);
                    log.info("任务:" + taskId + "已完成！清除缓存。" + "\n剩余缓存任务:" + completeEntityMap.keySet().toString());
                    pool.destroy();
                    break;
                }
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return taskId;
    }

    /**
     * 打印任务完成度（每秒1次）
     *
     * @param taskId
     */
    public static void showTaskCompleted(String taskId) {
        TaskEntity entity = completeEntityMap.get(taskId);
        if (null == entity) {
            return;
        }
        // 需加锁，否则数据累加将出错（多线程同时+1的情况，将导致累加值少于最大值）
        long currentTime = System.currentTimeMillis();
        synchronized (taskId) {
            entity.setCurrentNum(entity.getCurrentNum() + 1);
            long timeInterval = currentTime - entity.getLogTime();
            // 设置记录日志间隔，最快1s一次
            if (timeInterval < CompleteUtil.LOG_TIME_INTERVAL) {
                return;
            } else {
                entity.setLogTime(currentTime);
            }
        }
        BigDecimal time = new BigDecimal(currentTime - entity.getStartTime())
                .divide(new BigDecimal(1000), 1, BigDecimal.ROUND_HALF_UP);

        // 除法时若不指定小数位数（为保证精度，保留4位小数，等同于乘100后保留两位小数），可能出现无限循环
        BigDecimal bdComplete = new BigDecimal(entity.getCurrentNum())
                .divide(new BigDecimal(entity.getMaxNum()), 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
        String remainMsg = " 正在计算剩余时间...";
        // 执行到10%或者执行时间大于15秒时显示剩余时间
        if (bdComplete.doubleValue() > CompleteUtil.TIME_REMAIN_COMPLETE_PERCENT
                || time.doubleValue() > CompleteUtil.TIME_REMAIN_SECONDS_RUN) {
            BigDecimal remainTime = new BigDecimal(entity.getMaxNum() - entity.getCurrentNum())
                    .divide(new BigDecimal(entity.getCurrentNum()), 1, BigDecimal.ROUND_HALF_UP)
                    .multiply(time);
            remainMsg = "；预计剩余：" + TimeUnitTransferUtil.transferUnit(remainTime.longValue());
        }
        log.info(
                // 任务id只展示后6位
                "taskId:" + taskId.substring(taskId.length() - 6) +
                        "；总任务数：" + entity.getMaxNum() +
                        "；已完成：" + bdComplete.toString() +
                        "%；耗时：" + TimeUnitTransferUtil.transferUnit(time.longValue()) + remainMsg);
    }

}

@Data
class TaskEntity {

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 开始时间
     */
    private long startTime;

    /**
     * 记录日志时间
     */
    private long logTime;

    /**
     * 当前数据游标
     */
    private int currentNum;

    /**
     * 最大值（计算百分比用）
     */
    private int maxNum;

}
