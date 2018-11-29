package lock;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;
import java.util.concurrent.locks.*;

/**
 * Created by cuixiaodong on 2017/3/23.
 */
public class TestLock {
    @Test
    public void testLock() {
        PricesInfo pricesInfo = new PricesInfo();

        Reader[] readers = new Reader[5];
        Thread[] readerThread = new Thread[5];
        for (int i=0; i<5; i++){
            readers[i]=new Reader(pricesInfo);
            readerThread[i]=new Thread(readers[i]);
        }

        Writer writer=new Writer(pricesInfo);
        Thread threadWriter=new Thread(writer);

        for (int i=0; i<5; i++){
            readerThread[i].start();
        }
        threadWriter.start();
    }

    @Test
    public void testCy() {
        CyclicBarrierTest_1.main(new String[]{});
    }

    @Test
    public void testCountLatchDown() {

    }

    public static void main(String[] args) {
        List<String> buffer1 = new ArrayList<>();
        List<String> buffer2 = new ArrayList<>();

        Exchanger<List<String>> exchanger = new Exchanger<>();

        Producer producer = new Producer(buffer1, exchanger);
        Consumer consumer = new Consumer(buffer2, exchanger);

        Thread thread1 = new Thread(producer);
        Thread thread2 = new Thread(consumer);

        thread1.start();
        thread2.start();
    }
}
/*class testSem{
    public static void main(String[] args) {
        Thread[] threads = new Thread[10];

        PrintQueue printQueue = new PrintQueue();

        for(int i = 0 ; i < 10 ; i++){
            threads[i] = new Thread(new Job(printQueue),"Thread_" + i);
        }

        for(int i = 0 ; i < 10 ; i++){
            threads[i].start();
        }
    }
}
class PrintQueue {
    private final Semaphore semaphore;   //声明信号量

    public PrintQueue(){
        semaphore = new Semaphore(2);
    }

    public void printJob(Object document){
        try {
            semaphore.acquire();//调用acquire获取信号量
            long duration = (long) (Math.random() * 10);
            System.out.println( Thread.currentThread().getName() +
                                        "PrintQueue : Printing a job during " + duration);
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }  finally{
            semaphore.release();  //释放信号量
        }
    }
}
class Job implements Runnable{
    private PrintQueue printQueue;

    public Job(PrintQueue printQueue){
        this.printQueue = printQueue;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Going to print a job");
        printQueue.printJob(new Object());
        System.out.println(Thread.currentThread().getName() + " the document has bean printed");
    }
}*/
/*class TestCondition {
    public static void main(String[] args) {
        Depot depot = new Depot();

        Producer1 producer = new Producer1(depot);
        Customer1 customer = new Customer1(depot);

        producer.produce(10);
        customer.consume(5);
        producer.produce(15);
        customer.consume(10);
        customer.consume(15);
        producer.produce(10);
    }
}*/
class Producer implements Runnable{

    /**
     * 生产者和消费者进行交换的数据结构
     */private List<String> buffer;

    /**
     * 同步生产者和消费者的交换对象
     */private final Exchanger<List<String>> exchanger;

    Producer(List<String> buffer,Exchanger<List<String>> exchanger){
        this.buffer = buffer;
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        int cycle = 1;
            for (int i = 0; i < 10; i++) {
                System.out.println("Producer : Cycle :" + cycle);
                for (int j = 0; j < 10; j++) {
                    String message = "Event " + ((i * 10) + j);
                    System.out.println("Producer : " + message);
                    buffer.add(message);
                }
                //调用exchange()与消费者进行数据交换
                try {
                    buffer = exchanger.exchange(buffer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Producer :" + buffer.size());
                cycle++;
            }
    }
}
class Producer1 {
    private Depot depot;

    public Producer1(Depot depot){
        this.depot = depot;
    }

    public void produce(final int value){
        new Thread(){
            public void run(){
                depot.put(value);
            }
        }.start();
    }
}
class Customer1 {
    private Depot depot;

    public Customer1(Depot depot){
        this.depot = depot;
    }

    public void consume(final int value){
        new Thread(){
            public void run(){
                depot.get(value);
            }
        }.start();
    }
}

class Depot {
    private int depotSize;     //仓库大小
    private Lock lock;         //独占锁
    private int capaity;       //仓库容量

    private Condition fullCondition;
    private Condition emptyCondition;

    public Depot(){
        this.depotSize = 0;
        this.lock = new ReentrantLock();
        this.capaity = 15;
        this.fullCondition = lock.newCondition();
        this.emptyCondition = lock.newCondition();
    }

    /**
     * 商品入库
     * @param value
     */
    public void put(int value){
        lock.lock();
        try {
            int left = value;
            while(left > 0){
                //库存已满时，“生产者”等待“消费者”消费
                while(depotSize >= capaity){
                    fullCondition.await();
                }
                //获取实际入库数量：预计库存（仓库现有库存 + 生产数量） > 仓库容量   ? 仓库容量 - 仓库现有库存     :    生产数量
                //                  depotSize   left   capaity  capaity - depotSize     left
                int inc = depotSize + left > capaity ? capaity - depotSize : left;
                depotSize += inc;
                left -= inc;
                System.out.println(Thread.currentThread().getName() + "----要入库数量: " + value +";;实际入库数量：" + inc + ";;仓库货物数量：" + depotSize + ";;没有入库数量：" + left);

                //通知消费者可以消费了
                emptyCondition.signal();
            }
        } catch (InterruptedException e) {
        } finally{
            lock.unlock();
        }
    }

    /**
     * 商品出库
     * @param value
     */
    public void get(int value){
        lock.lock();
        try {
            int left = value;
            while(left > 0){
                //仓库已空，“消费者”等待“生产者”生产货物
                while(depotSize <= 0){
                    emptyCondition.await();
                }
                //实际消费      仓库库存数量     < 要消费的数量     ?   仓库库存数量     : 要消费的数量
                int dec = depotSize < left ? depotSize : left;
                depotSize -= dec;
                left -= dec;
                System.out.println(Thread.currentThread().getName() + "----要消费的数量：" + value +";;实际消费的数量: " + dec + ";;仓库现存数量：" + depotSize + ";;有多少件商品没有消费：" + left);

                //通知生产者可以生产了
                fullCondition.signal();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            lock.unlock();
        }
    }
}
class Consumer implements Runnable{
    private List<String> buffer;

    private final Exchanger<List<String>> exchanger;

    public Consumer(List<String> buffer,Exchanger<List<String>> exchanger){
        this.buffer = buffer;
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        int cycle = 1;
            for (int i = 0; i < 10; i++) {
                System.out.println("Consumer : Cycle :" + cycle);
                //调用exchange()与消费者进行数据交换
                try {
                    buffer = exchanger.exchange(buffer);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            System.out.println("Consumer :" + buffer.size());
            for (int j = 0; j < 10; j++) {
                System.out.println("Consumer : " + buffer.get(0));
                buffer.remove(0);
            }
            cycle++;
        }
    }
}
/*class countDownlatchTest {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        for(int i=0;i<5;i++){
            new Thread(new readNum(i,latch)).start();
        }
        Thread.currentThread().sleep(2000);
        latch.countDown();
        System.out.println("线程执行结束。。。。");
    }

    static class readNum  implements Runnable{
        private int id;
        private CountDownLatch latch;
        public readNum(int id,CountDownLatch latch){
            this.id = id;
            this.latch = latch;
        }
        @Override
        public void run() {
            synchronized (this){
                System.out.println("id:"+id);
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程组任务"+id+"结束，其他任务继续");
            }
        }
    }
}*/
class CyclicBarrierTest_1 {
    private static CyclicBarrier barrier;

    static class threadTest1 extends Thread{
        public void run() {
            System.out.println(Thread.currentThread().getName() + "达到...");
            try {
                barrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "执行完成...");
        }
    }

    public static void main(String[] args) {
        barrier = new CyclicBarrier(5,new Runnable() {
            @Override
            public void run() {
                System.out.println("执行CyclicBarrier中的任务.....");
            }
        });
        for(int i = 1 ; i <= 5 ; i++){
            new threadTest1().start();
        }

    }
}
class Reader implements Runnable{

    private PricesInfo pricesInfo;

    public Reader(PricesInfo pricesInfo){
        this.pricesInfo = pricesInfo;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + "reader--Price 1:" + pricesInfo.getPrice1());
            System.out.println(Thread.currentThread().getName() + "reader--Price 1:" + pricesInfo.getPrice2());
        }
    }

}
class Writer implements Runnable{
    private PricesInfo pricesInfo;

    public Writer(PricesInfo pricesInfo){
        this.pricesInfo = pricesInfo;
    }

    @Override
    public void run() {
        for (int i=0; i<3; i++) {
            System.out.printf("Writer: Attempt to modify the prices.\n");
            try {
                pricesInfo.setPrices(Math.random()*10, Math.random()*8);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Writer: Prices have been modified.\n");
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
class PricesInfo {
    private double price1;
    private double price2;

    private ReadWriteLock lock;

    public PricesInfo(){
        price1 = 1.0;
        price2 = 2.0;

        lock = new ReentrantReadWriteLock();
    }

    public double getPrice1(){
        lock.readLock().lock();
        double value = price1;
        lock.readLock().unlock();
        return value;
    }

    public double getPrice2(){
        lock.readLock().lock();
        double value = price2;
        lock.readLock().unlock();
        return value;
    }

    public void setPrices(double price1, double price2) throws InterruptedException {
        lock.writeLock().lock();
        this.price1 = price1;
        this.price2 = price2;
        lock.writeLock().unlock();
    }
}
class RunIt3 implements Runnable{

    private static Lock lock = new ReentrantLock();
    public void run(){
        try{
            //---------------------------------a
            lock.lock();
            //lock.tryLock();
            //lock.lockInterruptibly();
            /*if (!b) {
                System.out.println("fail");
            }*/
            //lock.lockInterruptibly();


            System.out.println(Thread.currentThread().getName() + " running");
            Thread.currentThread().sleep(20000);
            lock.unlock();
            System.out.println(Thread.currentThread().getName() + " finished");
        }
        catch (InterruptedException e){
            System.out.println(Thread.currentThread().getName() + " interrupted");

        }

    }
}
class TTT{
    public static void main(String[] args){
        RunIt3 runIt3 = new RunIt3();
        Thread i1 = new Thread(runIt3);
        Thread i2 = new Thread(runIt3);
        i1.start();
        i2.start();
        i2.interrupt();
    }

}