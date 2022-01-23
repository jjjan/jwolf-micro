package multi_thread;

import cn.hutool.core.thread.ThreadUtil;
import common.AbstractMultiThreadTask;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.CountDownLatch;

/**
 * 1.CountDownLatch，计数到0后主解除阻塞
 * 2.CyclicBarrier阻塞子线程，全部就绪后执行回调方法，然后继续各个子线程之后的逻辑
 */
public class T070CountDownLatch extends AbstractMultiThreadTask {

    public static void main(String[] args) {
        new T070CountDownLatch().doTask();
    }

    @Override
    public void taskContent(ThreadPoolTaskExecutor taskExecutor) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(4);
        new Thread(() -> {
            ThreadUtil.sleep(3333);
            System.out.println("准备停止线程池");
            taskExecutor.shutdown();
            //taskExecutor.getThreadPoolExecutor().shutdownNow();
        });

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            taskExecutor.submit(() -> {
                if (finalI == 1) {
                    int aa = 1 / 0;
                }
                try {
                    Thread.sleep(444);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(finalI + "xxxx");
                countDownLatch.countDown();
            });

        }
        //线程池被关闭，提交的线程池任务抛出中断异常并解除阻塞，继续进行后续任务，不会立即终止
        countDownLatch.await();
        System.out.println("main已解除阻塞");

    }


}