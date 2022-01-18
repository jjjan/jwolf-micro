package juc;


/**
 * 懒汉双重检验式，volatile保证线程可见,synchronize保证第二次检验不会同时多个线程读取到
 * single=null的情况；也可以直接同步方法，但synchronize较重，使用双重检验可以减少锁竞争
 *
 * new LazySingle()并不是原子操作，含以下几步，极小可能1->3->2导致single短暂为空，加volatile可以禁止重排序，防止返回未初始化的single，同时保证初始化后的single被其他线程立即看到
 * 1 LazySingle分配内存空间
 * 2.LazySingle对象初始化
 * 3.将single指向分配的内存地址
 */
    public  class T2LazySingle {

        private volatile static T2LazySingle single;

        private T2LazySingle() {
        }

        public static T2LazySingle getInstance() {
            if (single == null) {
                synchronized (T2LazySingle.class) {
                    if (single == null) {
                        single = new T2LazySingle();
                    }
                }
            }
            return single;
        }
    }