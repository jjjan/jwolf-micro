##知识点1.volitile:Java提供了轻量级同步机制，保证可见性（1个线程修改了变量值其它线程要同步知道的特性），不保证原子性，禁止指令重排
![](src/../../doc/doc-resource/test/内存模型.png)

[参考代码1](src/main/java/multi_thread/T1VolatileTest.java)

[参考代码2](src/main/java/multi_thread/T2LazySingle.java)

##知识点2.CopyOnWriteArraySet的底层实现与特性
```
  *它是线程安全的无序的集合，可以将它理解成线程安全的HashSet。CopyOnWriteArraySet和HashSet虽然都继承于共同的父类AbstractSet；
     * 但是，HashSet通过源码可见是“散列表(HashMap)”实现的,只是只关心KV的K，而CopyOnWriteArraySet则是通过“动态数组(CopyOnWriteArrayList)”实现的，并不是散列表。
     * 和CopyOnWriteArrayList类似，CopyOnWriteArraySet具有以下特性：
     * 1. 它最适合于具有以下特征的应用程序：Set 大小通常保持很小，只读操作远多于可变操作，需要在遍历期间防止线程间的冲突。
     * 2. 它是线程安全的。
     * 3. 因为通常需要复制整个基础数组，所以可变操作（add()、set() 和 remove() 等等）的开销很大。
     * 4. 迭代器支持hasNext(), next()等不可变操作，但不支持可变 remove()等 操作。
     * 5. 使用迭代器进行遍历的速度很快，并且不会与其他线程发生冲突。在构造迭代器时，迭代器依赖于不变的数组快照

```
 [参考代码1](src/main/java/multi_thread/T4CopyOnWriteArrayList.java)
##知识点3.ReentrantLock,synchronize锁默认都是可重入锁，默认非公平
重入锁特性：线程A获取锁进入method1，method1里面再调用method2，这里线程池A可以直接获取
method2的锁，因为synchronize是可重入锁，method1与method2使用的同把锁（this对象锁,如果两个方法是static则使用的是类锁）
```
   private synchronized void method1() {
        System.out.println(Thread.currentThread().getId()+"<<method1");
        method2();

    }
    private synchronized void method2() {
        System.out.println(Thread.currentThread().getId()+"<<method2");
    }

```

##知识点4.concurrentHashMap