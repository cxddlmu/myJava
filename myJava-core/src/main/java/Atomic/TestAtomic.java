package Atomic;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * Created by cuixiaodong on 2017/3/28.
 */
public class TestAtomic {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(10);
        atomicInteger.addAndGet(1);
        System.out.println(atomicInteger.incrementAndGet());
        System.out.println(atomicInteger.getAndIncrement());
        System.out.println(atomicInteger.getAndSet(100));
        System.out.println(atomicInteger.compareAndSet(100,200));
        System.out.println(atomicInteger.get());
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(10);
        atomicIntegerArray.set(0, 0);
        atomicIntegerArray.set(9, 9);
        System.out.println(atomicIntegerArray.incrementAndGet(9));
        System.out.println(atomicIntegerArray.getAndIncrement(9));
        System.out.println(atomicIntegerArray.getAndSet(9, 1000));
        System.out.println(atomicIntegerArray.get(9));
        TestAtomic demo = new TestAtomic();
        demo.doit();

    }

    class DemoData {
        public volatile int value1 = 1;
        volatile int value2 = 2;
        protected volatile int value3 = 3;
        private volatile int value4 = 4;
    }

    AtomicIntegerFieldUpdater getUpdater(String fieldName) {
        return AtomicIntegerFieldUpdater.newUpdater(DemoData.class, fieldName);
    }

    void doit() {
        DemoData data = new DemoData();
        System.out.println("1 ==< " + getUpdater("value1").getAndSet(data, 10));
        System.out.println("3 ==< " + getUpdater("value2").incrementAndGet(data));
        System.out.println("2 ==< " + getUpdater("value3").decrementAndGet(data));
        System.out.println("true ==< " + getUpdater("value4").compareAndSet(data, 4, 5));
    }
}

