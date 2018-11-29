package lock;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by cuixiaodong on 2017/3/27.
 */
public class TestCondition {
    ReentrantLock lock = new ReentrantLock();
    Condition conditionPrd = lock.newCondition();
    Condition conditionCus = lock.newCondition();
    AtomicInteger atomicInteger = new AtomicInteger(0);

    ConcurrentLinkedQueue<String> stringList = new ConcurrentLinkedQueue<>();
//    private volatile boolean threadAvailable = false;

    public void set() throws InterruptedException {
        lock.lock();
        int i = 0;
        while (true) {
            if (stringList.size() >= 5) {
                conditionCus.signalAll();
                conditionPrd.await();
            }
            Thread.sleep(500L);
            String s = "apple|" + i++;
            stringList.offer(s);
            System.out.println("set:" + s);

        }
    }

    public void get() throws InterruptedException, BrokenBarrierException {
        lock.lock();
        while (true) {
            Thread.sleep(500L);
            if (stringList.isEmpty()) {
                conditionPrd.signal();
                System.out.println(Thread.currentThread().getName() + " wait");
                conditionCus.await();
            }
            System.out.println(Thread.currentThread().getName() + " get: " + stringList.poll());

        }
    }

    public static void main(String[] args) throws InterruptedException {
        TestCondition testCondition1 = new TestCondition();

        new Thread(() -> {
            try {
                testCondition1.set();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                testCondition1.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }).start();
    }

}

