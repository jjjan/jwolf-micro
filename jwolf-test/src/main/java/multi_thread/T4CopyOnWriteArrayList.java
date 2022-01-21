package multi_thread;

import common.AbstractMultiThreadTask;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * public boolean add(E var1) {
 *         ReentrantLock var2 = this.lock; //复制前加锁
 *         var2.lock();
 *
 *         boolean var6;
 *         try {
 *             Object[] var3 = this.getArray();
 *             int var4 = var3.length;
 *             Object[] var5 = Arrays.copyOf(var3, var4 + 1); //复制,由此可见只适合读多写少场景，否则资源消耗大
 *             var5[var4] = var1;
 *             this.setArray(var5);
 *             var6 = true;
 *         } finally {
 *             var2.unlock(); //写完后解锁
 *         }
 *
 *         return var6;
 *     }
 *
 * @author majun
 * @version 1.0
 * @date 2022-01-18 22:14
 */
public class T4CopyOnWriteArrayList extends AbstractMultiThreadTask {


    @Override
    public void taskContent(ThreadPoolTaskExecutor taskExecutor) throws InterruptedException {
        //List<String> list = new ArrayList<>();
        //List<String> list = Collections.synchronizedList(new ArrayList<>());
        //List<String> list =new Vector<>();
        List<String> list = new CopyOnWriteArrayList<>();
        int corePoolSize = taskExecutor.getCorePoolSize();
        CountDownLatch countDownLatch = new CountDownLatch(corePoolSize);
        for (int i = 0; i < corePoolSize; i++) {
            taskExecutor.submitListenable(() -> {
                for (int j = 0; j < 10000; j++) {
                    list.add(Thread.currentThread().getName());
                    int a=1/0;
                }
                return list.size();
            }).addCallback(
                    o -> {
                        System.out.println(Thread.currentThread().getName() + "返回:" + o);
                        countDownLatch.countDown();
                    },
                    e -> {
                        e.printStackTrace();
                        countDownLatch.countDown();
                    });
        }
        countDownLatch.await();


    }

    public static void main(String[] args) throws Exception {
        new T4CopyOnWriteArrayList().doTask();
    }
}
