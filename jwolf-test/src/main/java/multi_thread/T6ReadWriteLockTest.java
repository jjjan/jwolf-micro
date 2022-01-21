package multi_thread;

import cn.hutool.core.thread.ThreadUtil;
import common.AbstractMultiThreadTask;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁，场景：如买车票与查看车票，如果两个过程都加写锁，查看车票会相互阻塞，导致并发低，都加读锁（相当于不锁），或导致
 * 查看到的车票与实际剩余车票不一致，导致幻读，可以使用一对读写锁：买车票加写锁，查看车票加读锁，这样可以并发读，读写，写写互斥，
 */
public class T6ReadWriteLockTest extends AbstractMultiThreadTask {
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public static void main(String[] args) {
        new T6ReadWriteLockTest().doTask();
    }

    @Override
    public void taskContent(ThreadPoolTaskExecutor taskExecutor) throws Exception {
        while (true) {
            taskExecutor.submitListenable(() -> {
                look();
            });
            taskExecutor.submitListenable(() -> {
                buy();
            });

        }
    }

    private void look() {
        //ReentrantReadWriteLock.WriteLock lock = readWriteLock.writeLock();
        ReentrantReadWriteLock.ReadLock lock = readWriteLock.readLock();
        if (lock.tryLock()) {
            try {
                System.out.println(Thread.currentThread().getName() + "查看车票");
                ThreadUtil.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();

            }

        } else {
            System.out.println(Thread.currentThread().getName() + "查看车票被阻塞，因为有人在买票");
        }
    }

    private void buy() {
        // ReentrantReadWriteLock.ReadLock lock = readWriteLock.readLock();
        ReentrantReadWriteLock.WriteLock lock = readWriteLock.writeLock();
        ThreadUtil.sleep(2222);
        if (lock.tryLock()) {
            try {
                System.out.println(Thread.currentThread().getName() + "购买车票");
                ThreadUtil.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();

            }
        } else {
            System.out.println(Thread.currentThread().getName() + "购买车票被阻塞，因为有人在买票或看票");
        }
    }
}