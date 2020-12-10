package com.hawk.nacos.demo.springboot.util;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * 时间单位转化
 *
 * @author Hawk
 * @date 2020/12/4
 */
public class TimeUnitTransferUtil {

    /**
     * 转化时间单位
     *
     * @param seconds
     * @return
     */
    public static String transferUnit(long seconds) {
        StringBuffer stringBuffer = new StringBuffer();
        exec(seconds, stringBuffer);
        return stringBuffer.toString();
    }

    public static void main(String[] args) {
        System.out.println(transferUnit(3600));
    }

    private static void exec(long seconds, StringBuffer stringBuffer) {
        int minuteSeconds = 60;
        int hourSeconds = minuteSeconds * 60;
        int daySeconds = hourSeconds * 24;
        if (seconds < 0) {
            throw new RuntimeException("传入时间出错");
        } else if (seconds < minuteSeconds) {
            // 一分钟内
            if (seconds != 0 || StringUtils.isEmpty(stringBuffer.toString())) {
                // 0秒需要展示
                stringBuffer.append(seconds + "秒");
            }
        } else if (seconds < hourSeconds) {
            // 一小时内
            BigDecimal[] bigDecimals = new BigDecimal(seconds).divideAndRemainder(new BigDecimal(minuteSeconds));
            int minutes = bigDecimals[0].intValue();
            long remainSeconds = bigDecimals[1].longValue();
            stringBuffer.append(minutes + "分钟");
            exec(remainSeconds, stringBuffer);
        } else if (seconds < daySeconds) {
            // 一天内
            BigDecimal[] bigDecimals = new BigDecimal(seconds).divideAndRemainder(new BigDecimal(hourSeconds));
            int hours = bigDecimals[0].intValue();
            long remainSeconds = bigDecimals[1].longValue();
            stringBuffer.append(hours + "小时");
            exec(remainSeconds, stringBuffer);
        } else if (seconds >= daySeconds) {
            BigDecimal[] bigDecimals = new BigDecimal(seconds).divideAndRemainder(new BigDecimal(daySeconds));
            // 大于一天
            int days = bigDecimals[0].intValue();
            long remainSeconds = bigDecimals[1].longValue();
            stringBuffer.append(days + "天");
            exec(remainSeconds, stringBuffer);
        }

    }

}
