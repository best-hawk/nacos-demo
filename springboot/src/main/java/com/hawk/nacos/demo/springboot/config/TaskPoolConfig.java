package com.hawk.nacos.demo.springboot.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 虽然线程池是构建多线程应用程序的强大机制，但使用它并不是没有风险的。用线程池构建的应用程序容易遭受任何其它多线程应用程序容易遭受的所有并发风险
 * 风险包括：死锁、资源不足，并发错误，线程泄漏，请求过载
 *
 * @author Hawk
 * @date 2020/12/2
 */
@Configuration
@EnableAsync
public class TaskPoolConfig implements AsyncConfigurer {

    /**
     * 数据格式化为M为单位
     *
     * @param s
     * @return
     */
    private static String mb(long s) {
        return String.format("%d (%.2f M)", s, (double) s / (1024 * 1024));
    }

    /**
     * 数据格式化为G为单位
     *
     * @param s
     * @return
     */
    private static String gb(long s) {
        return String.format("%d (%.2f G)", s, (double) s / (1024 * 1024 * 1024));
    }

    @Bean(name = "asyncServiceExecutor")
    public Executor asyncServiceExecutor() {
        /**
         * ThreadPoolTaskExecutor是spring core包中的，而ThreadPoolExecutor是JDK中的JUC。
         * ThreadPoolTaskExecutor是对ThreadPoolExecutor进行了封装处理。
         *
         * 如果当前运行的线程数小于corePoolSize，那么就创建线程来执行任务（执行时需要获取全局锁）
         * 如果运行的线程大于或等于corePoolSize，那么就把task加入BlockQueue
         * 如果创建的线程数量大于BlockQueue的最大容量，那么创建新线程来执行该任务
         * 如果创建线程导致当前运行的线程数超过maximumPoolSize，就根据饱和策略来拒绝该任务
         */
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 线程池核心线程数 4核8线程 设置核心线程50左右达到最佳值，继续往上设置无效果
        taskExecutor.setCorePoolSize(50);
        // 任务队列大小
        taskExecutor.setQueueCapacity(1000000);
        // 线程池维护线程的最大数量
        taskExecutor.setMaxPoolSize(100);
        // 空闲线程的存活时间，单位：秒
        taskExecutor.setKeepAliveSeconds(3);
        // 线程前缀名
        taskExecutor.setThreadNamePrefix("taskExecutor--");
        /**
         * 线程池已经饱和或
         * 拒绝策略：
         *  AbortPolicy：默认的策略,处理程序遭到拒绝将抛出运行时 RejectedExecutionException
         *  CallerRunsPolicy：直接在execute方法的调用线程中运行被拒绝的任务
         *  DiscardOldestPolicy：如果执行程序尚未关闭，则位于工作队列头部的任务将被删除，然后重试执行程序（如果再次失败，则重复此过程）
         *  DiscardPolicy：丢弃被拒绝的任务
         */
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }

    public static void main(String[] args) {
        System.out.println("程序最大可以使用的内存: " + mb(Runtime.getRuntime().maxMemory()));
        MemoryMXBean m = ManagementFactory.getMemoryMXBean();

        System.out.println("Non-heap: " + mb(m.getNonHeapMemoryUsage().getMax()));
        System.out.println("Heap: " + mb(m.getHeapMemoryUsage().getMax()));

        for (MemoryPoolMXBean mp : ManagementFactory.getMemoryPoolMXBeans()) {
            System.out.println("Pool: " + mp.getName() + " (type " + mp.getType() + ")" + " = " + mb(mp.getUsage().getMax()));
        }
    }

}
