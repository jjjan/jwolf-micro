package multi_thread;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * Description: TODO
 *
 * @author majun
 * @version 1.0
 * @date 2022-01-18 22:14
 */
public class T030AtomicAndCAS {
    public static void main(String[] args) {
        //CAS缺点: 1.ABA问题；2.轻量，但如果并发过高，CAS会长时间自旋导致CPU消耗高 3.只能保证单个成员变量原子性，同时操作多个变量还是得用锁
        //内置原子类仅AtomicInteger，底层依赖CAS，volatile，原子类很有限，可以自定义类型的原子类AtomicInference<V>包装
        AtomicInteger i = new AtomicInteger(0);
        System.out.println(i.compareAndSet(0, 100));
        System.out.println(i.compareAndSet(100, 0));
        //这里0——>100->0,即出现CAS的ABA问题，如下一语句可以执行成功，它感受不到其实i值已变更过，因为被修改过的值又刚好修改回来了
        System.out.println(i.compareAndSet(0, 50));
        //如果业务不允许ABA中途有被修改过这种情况，解决办法：增加元数据版本记录（或变更时间戳）机制，考虑使用AtomicStampedReference
        String str1 = "aaa";
        String str2 = "bbb";
        AtomicStampedReference<String> n = new AtomicStampedReference<>(str1, 1);
        System.out.println(String.format("初始值%s版本号%s", n.getReference(), n.getStamp()));
        boolean b1 = n.compareAndSet(str1, str2, 1, 2);
        System.out.println(String.format("初始值%s版本号%s", n.getReference(), n.getStamp()));
        boolean b2 = n.compareAndSet(str2, str1, 2, 3);
        System.out.println(String.format("初始值%s版本号%s", n.getReference(), n.getStamp()));
        //期望值是str1但是版本号已经由原来的1改为了3，故更新失败
        boolean b = n.compareAndSet(str1, "xxx", 1, 2);
        System.out.println(String.format("%s,%s,%s", b1, b2, b));


    }
}
