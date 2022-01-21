package multi_thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 自定义自旋锁及测试
 */
public class T5SpinLock  {
    //自旋锁

    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    //加锁
    public void myLock() {
        Thread thread = Thread.currentThread();
        System.out.println(Thread.currentThread().getName()+"尝试获取锁");
        while (!atomicReference.compareAndSet(null, thread)) {
            System.out.println("有线程占用中");
        }
        System.out.println("获得锁了");
    }


    //解锁
    public void myUnLock() {
        Thread thread = Thread.currentThread();
        System.out.println(Thread.currentThread().getName()+"准备释放锁" );
        atomicReference.compareAndSet(thread, null);
         System.out.println("已释放锁");
    }

     public static void main(String[] args) throws InterruptedException {
//        ReentrantLock reentrantLock = new ReentrantLock();

        T5SpinLock spinLock = new T5SpinLock();
        new Thread(() -> {
            spinLock.myLock();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                spinLock.myUnLock();
            }

        }, "T1").start();

         TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            spinLock.myLock();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                spinLock.myUnLock();
            }

        }, "T2").start();

    }
}