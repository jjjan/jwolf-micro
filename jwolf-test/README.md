##知识点1.volitile:Java提供了轻量级同步机制，保证可见性（1个线程修改了变量值其它线程要同步知道的特性），不保证原子性，禁止指令重排
![](src/../../doc/doc-resource/test/内存模型.png)

[参考代码1](src/main/java/juc/T1VolatileTest.java)

[参考代码2](src/main/java/juc/T2LazySingle.java)

