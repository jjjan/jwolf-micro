package multi_thread;

import cn.hutool.core.thread.ThreadUtil;
import common.AbstractMultiThreadTask;
import lombok.SneakyThrows;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;

/**
 * 一、线程池提交任务三个API的区别
 *  1.execute抛出异常但不影响主线程及其它线程
 *  2.submit需要Futrue.get才能拿到异常，会导致主线程阻塞，如果get到异常，会抛到主线程
 *  3.spring增强的submitListenable，可在异常回调中拿到异常，且不会导致主线程阻塞及异常
 * 二、核心参数设置
 *  CPU密集可以设置逻辑核数+1，IO密集应该适当多一些，因为IO不消耗CPU
 *
 */
public class T10ThreadPool extends AbstractMultiThreadTask {

    public static void main(String[] args) {
        new T10ThreadPool().doTask();
    }

    @SneakyThrows
    @Override
    public void taskContent(ThreadPoolTaskExecutor taskExecutor) {
        for (int i = 0; i < 20; i++) {
            int finalI = i;
              taskExecutor.submitListenable(() -> {
                System.out.println("开始任务"+ finalI);
                Assert.isTrue(finalI!=5,"xxx error");
                ThreadUtil.sleep(3000);
                System.out.println("结束任务"+ finalI);

            }).addCallback(result -> {},ex -> ex.printStackTrace());
        }

    }


}