package multi_thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * Description: 不可重入公平锁
 *
 * @author majun
 * @version 1.0
 * @date 2022-01-25 00:13
 */
public class T062MyLock {
    AtomicReference<Thread> lockHolder = new AtomicReference();
    BlockingQueue<Thread> waiterQueue = new LinkedBlockingQueue();

    public void lock() {
        Thread thread = Thread.currentThread();
        while (!lockHolder.compareAndSet(null, thread)) {
            waiterQueue.add(thread);
            //这里不可以用wait notify，因为notify不能唤醒指定的线程，只能用LockSupport
            LockSupport.park();
            waiterQueue.remove(thread);
        }
    }

    public void unlock() {
        if (lockHolder.compareAndSet(Thread.currentThread(), null)) {
            waiterQueue.forEach(thread -> LockSupport.unpark(thread));
        }
    }
}