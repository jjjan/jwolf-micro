package multi_thread;

import common.AbstractMultiThreadTask;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * 1.核心源码
 * public boolean add(E var1) {
 * ReentrantLock var2 = this.lock; //复制前加锁
 * var2.lock();
 * <p>
 * boolean var6;
 * try {
 * Object[] var3 = this.getArray();
 * int var4 = var3.length;
 * Object[] var5 = Arrays.copyOf(var3, var4 + 1); //复制,由此可见只适合读多写少场景，否则资源消耗大
 * var5[var4] = var1;
 * this.setArray(var5);
 * var6 = true;
 * } finally {
 * var2.unlock(); //写完后解锁
 * }
 * <p>
 * return var6;
 * }
 * <p>
 * <p>
 * 2. OnWriteArraySet它是线程安全的无序的集合，可以将它理解成线程安全的HashSet。
 * 3.CopyOnWriteArraySet和HashSet虽然都继承于共同的父类AbstractSet； 但是，HashSet通过源码可见是“散列表(HashMap)”实现的,只是只关心KV的K，而CopyOnWriteArraySet则是通过“动态数组(CopyOnWriteArrayList)”实现的，并不是散列表。
 * 4.和CopyOnWriteArrayList类似，CopyOnWriteArraySet具有以下特性：
 * a.它最适合于具有以下特征的应用程序：Set 大小通常保持很小，只读操作远多于可变操作，需要在遍历期间防止线程间的冲突。
 * b. 因为通常需要复制整个基础数组，所以可变操作（add()、set() 和 remove() 等等）的开销很大。
 * c. 迭代器支持hasNext(), next()等不可变操作，但不支持可变 remove()等 操作。
 * d. 使用迭代器进行遍历的速度很快，并且不会与其他线程发生冲突。在构造迭代器时，迭代器依赖于不变的数组快照
 *
 * @author majun
 * @version 1.0
 * @date 2022-01-18 22:14
 */
public class T040CopyOnWriteArrayList extends AbstractMultiThreadTask {


    public static void main(String[] args) throws Exception {
        new T040CopyOnWriteArrayList().doTask();
    }

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
                    int a = 1 / 0;
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
}
