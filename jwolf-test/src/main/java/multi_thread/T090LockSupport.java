package multi_thread;

import cn.hutool.core.thread.ThreadUtil;
import common.AbstractMultiThreadTask;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.locks.LockSupport;

/**
 * LockSupport与中断
 */
public class T090LockSupport extends AbstractMultiThreadTask {

    public static void main(String[] args) {
        new T090LockSupport().doTask();
    }

    @Override
    public void taskContent(ThreadPoolTaskExecutor taskExecutor) {
        Thread t = Thread.currentThread();
        new Thread(() -> {
            ThreadUtil.sleep(1111);
            System.out.println("准备中断main线程");
            t.interrupt();
        }).start();

        LockSupport.park();//不会抛出中断异常，但会解除阻塞
        LockSupport.park();//中断状态的线程尝试阻塞会直接pass,不会消除中断状态
        LockSupport.park();//中断状态的线程尝试阻塞会直接pass,
        try {
            Thread.sleep(3333);//中断状态的线程调用sleep会直接抛出中断异常,如果被catch了，线程取消中断状态，所有下面的park会阻塞
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Thread.interrupted();//调用该方法也会取消中断状态
        LockSupport.park();
        System.out.println(11);

    }


}