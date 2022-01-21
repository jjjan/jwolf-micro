package multi_thread;

import cn.hutool.core.thread.ThreadUtil;
import common.AbstractMultiThreadTask;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 1.CountDownLatch阻塞的主线程，计数到0后主解除阻塞
 * 2.CyclicBarrier阻塞子线程，全部就绪后执行回调方法，然后继续各个子线程之后的逻辑；循环栅栏
 * 3.线程池关闭，sleep的线程能捕获中断异常，CountDownLatch,CyclicBarrier的wait不会自动解除阻塞,不同于 LockSupport
 */
public class T8CycleBarrier extends AbstractMultiThreadTask {

    public static void main(String[] args) {
        new T8CycleBarrier().doTask();
    }

    @Override
    public void taskContent(ThreadPoolTaskExecutor taskExecutor) throws Exception {
        int n = taskExecutor.getCorePoolSize();
        CyclicBarrier barrier = new CyclicBarrier(41, () -> {
            System.out.println("全部就绪");
        });
        // spring的taskExecutor会报错
        ExecutorService threadPool = Executors.newFixedThreadPool(n);
        new Thread(() -> {
            ThreadUtil.sleep(1111);

            System.out.println("准备停止线程池");
            threadPool.shutdownNow();
        }).start();
        for (int i = 0; i < n; i++) {
            threadPool.submit(() -> {
                System.out.println(Thread.currentThread().getName() + "is coming");
                try {
                    barrier.await();
                    System.out.println(Thread.currentThread().getName() + "开始打麻将");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }

    }


}