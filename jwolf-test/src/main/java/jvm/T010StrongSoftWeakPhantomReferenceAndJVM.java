package jvm;

import cn.hutool.core.thread.ThreadUtil;
import common.AbstractMultiThreadTask;
import lombok.SneakyThrows;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ArrayList;

/**
 * 强软弱虚
 * <p>
 * 大多数是强引用，内存不足，只要GCroot可达，也不会回收，即使OOM;软引用内存不足会回收，如Redis缓存的源码,缓存的对象在系统内存足够时放内存可提高OPS，不够时又能在GC时（即使GCroot可达）被垃圾回收掉，
 * 也可以使用弱引用，在第一次GC就被回收也是OK的,虚引用则在任何时候都可能被回收，适用场景不多
 * <p>
 * 测试环境参数GC收集器
 * <p>
 * VM options: -Xmx300m -Xmx300m -XX:+PrintGCDetails -XX:-ScavengeBeforeFullGC，否则会使FULL GC前会进行一次PSYoung GC
 * 年轻代=300×（1/3）=100M ,from/to=100M*(1/10)=10M
 * 新生代复制算法，老年代标记清除/整理，jdk8默认+UseParallelGC:年轻代自动UseParrallelScavengeGC，老年代会自动使用parallelOldGC；使用其它收集器如CMS（-XX:+UseConcMarkSweepGC:），年轻代自动UseParNewGC
 * 串行并行都stop the world,只是并行是多个清理垃圾的线程，并发标记清除CMS仅标记时短暂stop the world，清理时不阻塞用户进程*
 * G1:取代CMS，Java9默认，停顿更少，无内存碎片，停顿时间可控等，划分为N个大小相同的region，每个区域是动态的，可能是Eden,survicor,old,humongous大对象块
 * <p>
 * <p>
 * 案例与日志
 * <p>
 * 1.eden区<80M,每次分配20M大对象，eden区可容纳3个20M对象，首次因有较多其它初始的对象，第3个就无法分配了
 * 2.无法分配时触发有young GC (Allocation Failure)，但这些大对象无法复制到Survivor from区，直接进入老年代,老年代也无法分配,
 * 3.进行Full GC(FULL GC会进行年轻代，老年代，元空间垃圾回收)，之后仍无法分配，oom前再次触发FULL GC,这次尝试回收非强引用，之后仍无法分配才OOM，这里成功回收的弱引用
 * <p>
 * <p>
 * 22:32:12.954 [main] DEBUG org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor - Initializing ExecutorService
 * ------------
 * 第0个元素[Ljava.lang.Byte;@5ba23b66
 * ------------
 * ------------
 * 第0个元素[Ljava.lang.Byte;@5ba23b66
 * 第1个元素[Ljava.lang.Byte;@2c8d66b2
 * ------------
 * [GC (Allocation Failure) [PSYoungGen: 22874K->2908K(89600K)] 186714K->166756K(294400K), 0.0051659 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
 * [Full GC (Allocation Failure) [PSYoungGen: 2908K->0K(89600K)] [ParOldGen: 163848K->166576K(204800K)] 166756K->166576K(294400K), [Metaspace: 6327K->6327K(1056768K)], 0.0772872 secs] [Times: user=0.28 sys=0.00, real=0.08 secs]
 * [Full GC (Allocation Failure) [PSYoungGen: 0K->0K(89600K)] [ParOldGen: 166576K->2577K(204800K)] 166576K->2577K(294400K), [Metaspace: 6327K->6320K(1056768K)], 0.0151066 secs] [Times: user=0.02 sys=0.00, real=0.02 secs]
 * ------------
 * 第0个元素null
 * 第1个元素null
 * 第2个元素[Ljava.lang.Byte;@5a39699c
 * ------------
 * ------------
 * 第0个元素null
 * 第1个元素null
 * 第2个元素[Ljava.lang.Byte;@5a39699c
 * 第3个元素[Ljava.lang.Byte;@3cb5cdba
 * ------------
 * [GC (Allocation Failure) [PSYoungGen: 3072K->32K(89600K)] 169489K->166449K(294400K), 0.0014708 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * [Full GC (Allocation Failure) [PSYoungGen: 32K->0K(89600K)] [ParOldGen: 166417K->165561K(204800K)] 166449K->165561K(294400K), [Metaspace: 6326K->6326K(1056768K)], 0.1553761 secs] [Times: user=0.33 sys=0.00, real=0.16 secs]
 * [Full GC (Allocation Failure) [PSYoungGen: 0K->0K(89600K)] [ParOldGen: 165561K->1721K(204800K)] 165561K->1721K(294400K), [Metaspace: 6326K->6326K(1056768K)], 0.0086110 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
 * ------------
 * 第0个元素null
 * 第1个元素null
 * 第2个元素null
 * 第3个元素null
 * 第4个元素[Ljava.lang.Byte;@56cbfb61
 * ------------
 * ------------
 * 第0个元素null
 * 第1个元素null
 * 第2个元素null
 * 第3个元素null
 * 第4个元素[Ljava.lang.Byte;@56cbfb61
 * 第5个元素[Ljava.lang.Byte;@1134affc
 * ------------
 * [GC (Allocation Failure) [PSYoungGen: 3072K->32K(89600K)] 168633K->165593K(294400K), 0.0273701 secs] [Times: user=0.06 sys=0.00, real=0.03 secs]
 * [Full GC (Allocation Failure) [PSYoungGen: 32K->0K(89600K)] [ParOldGen: 165561K->165559K(204800K)] 165593K->165559K(294400K), [Metaspace: 6327K->6327K(1056768K)], 0.1482740 secs] [Times: user=0.33 sys=0.00, real=0.15 secs]
 * [Full GC (Allocation Failure) [PSYoungGen: 0K->0K(89600K)] [ParOldGen: 165559K->1719K(204800K)] 165559K->1719K(294400K), [Metaspace: 6327K->6327K(1056768K)], 0.0101960 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
 * ------------
 * 第0个元素null
 * 第1个元素null
 * 第2个元素null
 * 第3个元素null
 * 第4个元素null
 * 第5个元素null
 * 第6个元素[Ljava.lang.Byte;@d041cf
 * ------------
 * ------------
 * 第0个元素null
 * 第1个元素null
 * 第2个元素null
 * 第3个元素null
 * 第4个元素null
 * 第5个元素null
 * 第6个元素[Ljava.lang.Byte;@d041cf
 * 第7个元素[Ljava.lang.Byte;@129a8472
 * ------------
 * [GC (Allocation Failure) [PSYoungGen: 3072K->32K(89600K)] 168631K->165599K(294400K), 0.0282445 secs] [Times: user=0.03 sys=0.00, real=0.03 secs]
 * [Full GC (Allocation Failure) [PSYoungGen: 32K->0K(89600K)] [ParOldGen: 165567K->165559K(204800K)] 165599K->165559K(294400K), [Metaspace: 6327K->6327K(1056768K)], 0.0682269 secs] [Times: user=0.14 sys=0.00, real=0.07 secs]
 * [Full GC (Allocation Failure) [PSYoungGen: 0K->0K(89600K)] [ParOldGen: 165559K->1719K(204800K)] 165559K->1719K(294400K), [Metaspace: 6327K->6327K(1056768K)], 0.0100719 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
 * ------------
 * 第0个元素null
 * 第1个元素null
 * 第2个元素null
 * 第3个元素null
 * 第4个元素null
 * 第5个元素null
 * 第6个元素null
 * 第7个元素null
 * 第8个元素[Ljava.lang.Byte;@1b0375b3
 * ------------
 * ------------
 * 第0个元素null
 * 第1个元素null
 * 第2个元素null
 * 第3个元素null
 * 第4个元素null
 * 第5个元素null
 * 第6个元素null
 * 第7个元素null
 * 第8个元素[Ljava.lang.Byte;@1b0375b3
 * 第9个元素[Ljava.lang.Byte;@2f7c7260
 * ------------
 * 22:32:23.840 [main] DEBUG org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor - Shutting down ExecutorService
 * done>>>>
 * Heap
 * PSYoungGen      total 89600K, used 2304K [0x00000000f9c00000, 0x0000000100000000, 0x0000000100000000)
 * eden space 76800K, 3% used [0x00000000f9c00000,0x00000000f9e401f0,0x00000000fe700000)
 * from space 12800K, 0% used [0x00000000ff380000,0x00000000ff380000,0x0000000100000000)
 * to   space 12800K, 0% used [0x00000000fe700000,0x00000000fe700000,0x00000000ff380000)
 * ParOldGen       total 204800K, used 165559K [0x00000000ed400000, 0x00000000f9c00000, 0x00000000f9c00000)
 * object space 204800K, 80% used [0x00000000ed400000,0x00000000f75add18,0x00000000f9c00000)
 * Metaspace       used 6337K, capacity 6550K, committed 6656K, reserved 1056768K
 * class space    used 684K, capacity 757K, committed 768K, reserved 1048576K
 */
public class T010StrongSoftWeakPhantomReferenceAndJVM extends AbstractMultiThreadTask {

    public static void main(String[] args) {
        new T010StrongSoftWeakPhantomReferenceAndJVM().doTask();
    }

    @SneakyThrows
    @Override
    public void taskContent(ThreadPoolTaskExecutor taskExecutor) {

        ArrayList<Reference> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            //强,即不用Reference包装，这时不断存入list，会OOM，这些对象GCroot可达。(虚拟机栈（方法入栈申明的局部引用类型变量即是gcroot,但会随着出栈消失而后被回收），static，final，native字段引用的对象等)
            //软引用，本例FULL GC前的Young GC不会回收，分配失败触发Full GC (Allocation Failure GC）准备OOM时回收
            //弱引用，一次Young GC时就可将其回收
            //虚引用，任何时候都可能被回收，get方法拿到的始终为null
            //几大引用再GC回收后都可暂存队列(第二个构造参数)，这样对象被GC回收时可通过队列拿到消息，进行一些逻辑
            Reference reference = new SoftReference(new Byte[1024 * 1024 * 20]);
            //Reference reference = new WeakReference(new Byte[1024 * 1024]);
            //Byte[] obj = new Byte[1024 * 1024 * 20];
            list.add(reference);
            System.out.println("------------");
            for (int j = 0; j < list.size(); j++) {
                System.out.println("第" + (j) + "个元素" + list.get(j).get());
            }
            System.out.println("------------");
            ThreadUtil.sleep(1000);
        }
    }


}