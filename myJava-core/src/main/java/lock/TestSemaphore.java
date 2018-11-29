package lock;

/**
 * Created by cuixiaodong on 2017/3/27.
 */

import java.util.concurrent.Semaphore;

public class TestSemaphore<T> {
    public static void main(String[] args) throws InterruptedException {
//        Thread[] threads = new Thread[10];
//
//        PrintQueue printQueue = new PrintQueue();
//
//        for (int i = 0; i < 10; i++) {
//            threads[i] = new Thread(new Job(printQueue), "Thread_" + i);
//        }
//
//        for (int i = 0; i < 10; i++) {
//            threads[i].start();
//            Thread.sleep(500);
//            if (i == 3) {
//                threads[i].interrupt();
//            }
//        }

        Service service = new Service();
        ThreadA a = new ThreadA(service);
        ThreadA b = new ThreadA(service);

        a.setName("a");
        b.setName("b");

        a.start();
        b.start();

//        Thread.sleep(1000);
        b.interrupt();

    }
}
 class ThreadA extends Thread{

    private Service service;

    public ThreadA(Service service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.testMethod();
    }
}

class PrintQueue {
    private final Semaphore semaphore;   //声明信号量
    private volatile boolean isShowAvailablePermits = true;
    private int cnt = 0;

    public PrintQueue() {
        semaphore = new Semaphore(1);

    }

    public void printJob(Object document) {
        try {
            System.out.println(Thread.currentThread().getName() + " wait");
            semaphore.acquireUninterruptibly();//调用acquire获取信号量
//            semaphore.acquire();//调用acquire获取信号量
//            semaphore.tryAcquire();//调用acquire获取信号量
            long duration = (long) (Math.random() * 10);
            System.out.println(Thread.currentThread().getName() +
                                       " invoke " + duration);
            System.out.println("availablePermits--init -" + semaphore.availablePermits());
            cnt++;

            Thread.sleep(1000L);

        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() +
                                       " is interrupt " );
            e.printStackTrace();
        } finally {
            semaphore.release();  //释放信号量
            if (cnt == 8) {
                semaphore.drainPermits();
            }
            System.out.println(Thread.currentThread().getName() + " finish");
            System.out.println("availablePermits---" + semaphore.availablePermits());
        }
    }
}

class Job implements Runnable {
    private PrintQueue printQueue;

    public Job(PrintQueue printQueue) {
        this.printQueue = printQueue;
    }

    @Override
    public void run() {

        printQueue.printJob(new Object());
    }
}

/**
 *
 * acquireUninterruptibly
 * get  a
 get  b
 b   exception block
 */
class Service {

    private Semaphore semaphore = new Semaphore(1);

    public void testMethod() {

        try {
            semaphore.acquireUninterruptibly();
            System.out.println("get  " + Thread.currentThread().getName());
            Thread.sleep(1000);
            semaphore.release();

        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + "   exception block");
        }
    }

}