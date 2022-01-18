package juc;

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
 * Description: TODO
 *
 * @author majun
 * @version 1.0
 * @date 2022-01-17 00:22
 */
public class T1VolatileTest {
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
        //如不保证可进行,此处将死循环
        while (count == 0) {
            //空转
        }
        System.out.println("有数据了");

        //2.测试原子性,volitile不保证原子性,可通过锁或原子类保证线程安全
        // 这里可以使用重入锁，写锁（读锁不具有排他性，可多个线程同时读，这里不能保证线程安全），同步锁，原子类来保证线程安装
        long t = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(4);
        ExecutorService pool = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            pool.submit(() -> {
                for (int j = 0; j < 100000; j++) {
                    synchronized (T1VolatileTest.class) {
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
