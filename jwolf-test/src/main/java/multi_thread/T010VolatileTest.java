package multi_thread;

import cn.hutool.core.thread.ThreadUtil;
import lombok.SneakyThrows;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * volatile为Java提供了轻量级同步机制，保证可见性（1个线程修改了变量值其它线程要同步知道的特性），不保证原子性，禁止指令重排
 * Java内存模型规定了所有的变量都存储在主内存中，每条线程还有自己的工作内存，线程的工作内存中保存了该线程中是用到的变量的主内存副本拷贝，线程对变量的所有操作都必须在工作内存中进行，而不能直接读写主内存。不同的线程之间也无法直接访问对方工作内存中的变量，线程间变量的传递均需要自己的工作内存和主存之间进行数据同步进行。
 *
 * @author majun
 * @version 1.0
 * @date 2022-01-17 00:22
 */
public class T010VolatileTest {
    private volatile static Integer count = 0;
    //private static Integer count=0;
    private static AtomicInteger count2 = new AtomicInteger(0);
    private static ReadWriteLock lock1 = new ReentrantReadWriteLock();
    private static ReentrantLock lock2 = new ReentrantLock();

    @SneakyThrows
    public static void main(String[] args) {
        //1.测试可见性
        new Thread(() -> {
            ThreadUtil.sleep(3000);
            count++;
        }, "AAA").start();
        //如不保证可见性,此处将死循环
        while (count == 0) {
            //空转
        }
        System.out.println("xxxxxxxxxxxxxx");

        //2.测试原子性,volitile不保证原子性,可通过锁或原子类保证线程安全
        // 这里可以使用重入锁，写锁（读锁不具有排他性，可多个线程同时读，这里不能保证线程安全），同步锁，原子类来保证线程安装
        long t = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(4);
        ExecutorService pool = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            pool.submit(() -> {
                for (int j = 0; j < 100000; j++) {
                    synchronized (T010VolatileTest.class) {
                        count++;
                    }
                    System.out.println(count);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        System.out.println(System.currentTimeMillis() - t);

    }


}
