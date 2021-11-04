package com.jwolf.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RetryUtils {

    public static <T> T retryWhenNull(RetryContent<T> handler, long timeoutMills, long sleepIntervalMills) {
        while (timeoutMills > 0) {
            T result = handler.retry();
            if (result != null) {
                return result;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(sleepIntervalMills);
            } catch (InterruptedException e) {
               log.warn("Interrupt when retry",e);
            }
            timeoutMills -= sleepIntervalMills;

        }
        return null;

    }

    public static <T> T retryWhenRuntimeException(RetryContent<T> handler, int tryCount,long sleepIntervalMills) {
        while (tryCount > 0) {
            try {
                return handler.retry();
            } catch (RuntimeException e1) {
                    log.warn("RuntimeException when retry",e1);
                try {
                    TimeUnit.MILLISECONDS.sleep(sleepIntervalMills);
                } catch (InterruptedException e2) {
                    log.warn("Interrupt when retry",e2);
                }
               tryCount--;
            } catch (Exception e3) {
                return null;
            }
        }
        return null;

    }


    public interface RetryContent<T> {
        //默认重试方法 TODO
        RetryContent<Integer> DEFAULT = () -> {
            int i = new Random().nextInt();
            System.out.println(i);
            return i > 10 ? i : null;
        };

        T retry();
    }


    public static void main(String[] args) {
        Integer random = RetryUtils.retryWhenNull(RetryContent.DEFAULT, 5000, 500);
        System.out.println("result is " + random);
    }
}
