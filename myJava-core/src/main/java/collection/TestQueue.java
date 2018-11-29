package collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.concurrent.*;

/**
 * Created by cuixiaodong on 2017/3/11.
 */

/**
 * Throws exception	Special value	Blocks	Times out
 * Insert	add(e)	offer(e)	put(e)	offer(e, time, unit)
 * Remove	remove()	poll()	take()	poll(time, unit)
 * Examine	element()	peek()	not applicable	not applicable
 */
enum QueueEnum {
    LinkedBlockingQueue(LinkedBlockingQueue.class.getName(), true, true,false),
    ArrayBlockingQueue(ArrayBlockingQueue.class.getName(), true, true,false),
    SynchronousQueue(SynchronousQueue.class.getName(), true, true,null),
    DelayQueue(DelayQueue.class.getName(), true, true,true),
    LinkedTransferQueue(LinkedTransferQueue.class.getName(), true, null,true),
    PriorityBlockingQueue(java.util.concurrent.PriorityBlockingQueue.class.getName(), true,true,true),
    PriorityQueue(java.util.PriorityQueue.class.getName(), false,false,true),
    ConcurrentLinkedQueue(java.util.concurrent.ConcurrentLinkedQueue.class.getName(), false,true,true);

    private QueueEnum(String className, Boolean blocked, Boolean threadSafe,Boolean unbound) {
        this.className = className;
        this.blocked = blocked;
    }

    public String getClassName() {
        return this.className;
    }

    private String className;
    private Boolean blocked;
}
enum DequeEnum {
    ArrayDeque(java.util.ArrayDeque.class.getName(), true, true, false),
    ConcurrentLinkedDeque(ConcurrentLinkedDeque.class.getName(), true, true,false),
    LinkedList(LinkedList.class.getName(), true, true, null),
    ;

    private DequeEnum(String className, Boolean blocked, Boolean threadSafe,Boolean unbound) {
        this.className = className;
        this.blocked = blocked;
    }

    public String getClassName() {
        return this.className;
    }

    private String className;
    private Boolean blocked;
}

public class TestQueue {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
//        final BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(3);//该队列里面只能放3个Integer
//        final BlockingQueue<Integer> queue = (BlockingQueue<Integer>) Class.forName(BlockQueueEnum.LinkedBlockingDeque.getClassName()).newInstance();
//        final BlockingQueue<Integer> queue = new LinkedBlockingDeque<>(3);//该队列里面只能放3个Integer
//        TestQueue.testLinkedBlockingDeque();
    }

    /**
     * 储物柜只能有一个快递
     */
    @Test
    public void testSynchronousQueue() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, InterruptedException {
        BlockingQueue queue = (SynchronousQueue) Class.forName(QueueEnum.SynchronousQueue.getClassName()).getConstructor().newInstance();
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep((long) (Math.random() * 1000));
                        System.out.println(Thread.currentThread().getName() + "准备放数据!");
                        queue.put(Thread.currentThread().getName());
                        System.out.println(Thread.currentThread().getName() + "已经放了数据，" +"队列目前有" + queue.size() + "个数据");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        //将此处的睡眠时间分别改为100和1000，观察运行结果
                        Thread.sleep(9000);
                        System.out.println(Thread.currentThread().getName() + "准备取数据!");
                        Object o = queue.poll();//如果没有了数据,就会阻塞
                        System.out.println(Thread.currentThread().getName() + "已经取走数据》》" +o+ ",队列目前有" + queue.size() + "个数据");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }.start();
        Thread.sleep(100000);
    }
    static void testLinkedBlockingDeque() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        BlockingQueue linkedBlockingDeque = (LinkedBlockingDeque) Class.forName("LinkedBlockingDeque").getConstructor(int.class).newInstance(3);
//        final BlockingQueue<Integer> queue = new LinkedBlockingDeque<>(3);//该队列里面只能放3个Integer
        for (int i = 0; i < 2; i++) {
            new Thread() {
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep((long) (Math.random() * 1000));
                            System.out.println(Thread.currentThread().getName() + "准备放数据!");

//                            queue.put(1);//如果放满就会阻塞
                            //queue.add(2);//full throw exception
//                            queue.offer(2);//return false
//                            System.out.println(Thread.currentThread().getName() + "已经放了数据，" +"队列目前有" + queue.size() + "个数据");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        //将此处的睡眠时间分别改为100和1000，观察运行结果
                        Thread.sleep(1000);
                        System.out.println(Thread.currentThread().getName() + "准备取数据!");
//                        Integer take = queue.take();//如果没有了数据,就会阻塞
//                        System.out.println(Thread.currentThread().getName() + "已经取走数据》》" +take+ ",队列目前有" + queue.size() + "个数据");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }.start();
    }

    /**
     *    阻塞快递员等待客户签收
     */
    void TestLinkedTransferQueue() {
        final BlockingQueue<Integer> queue = new LinkedTransferQueue<>();//该队列里面只能放3个Integer
        for (int i = 0; i < 2; i++) {
            new Thread() {
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep((long) (Math.random() * 1000));
                            System.out.println(Thread.currentThread().getName() + "准备放数据!");
                            queue.offer(2);//return false
                            ((LinkedTransferQueue) queue).transfer(2);
//                            System.out.println(Thread.currentThread().getName() + "已经放了数据，" +"队列目前有" + queue.size() + "个数据");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(3000);
                        System.out.println(Thread.currentThread().getName() + "准备取数据!");
                        Integer take = (Integer) ((LinkedTransferQueue) queue).poll();//
                        System.out.println(Thread.currentThread().getName() + "已经取走数据》》" + take + ",队列目前有" + queue.size() + "个数据");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * 按约定时间优先级收快递员
     */
    @Test
    public void TestDelayQueue() throws InterruptedException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        BlockingQueue delayQueue = (DelayQueue) Class.forName(QueueEnum.DelayQueue.getClassName()).getConstructor().newInstance();
        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        class DelayObj implements Delayed {
            private String name;
            private int time;

            @Override
            public long getDelay(TimeUnit unit) {
                return time;
            }

            @Override
            public int compareTo(Delayed o) {
//                return -(((DelayObj) o).getTime() - this.getTime());
                return getDelay(TimeUnit.MINUTES) - o.getDelay(TimeUnit.MINUTES)>0?1:-1;
            }
        }

        for (int i = 0; i < 2; i++) {
            new Thread() {
                public void run() {
//                    while (true) {
                        try {
                            Thread.sleep((long) (Math.random() * 1000));
                            System.out.println(Thread.currentThread().getName() + "准备放数据!");
                            DelayObj delayObj = new DelayObj("good",10);
                            DelayObj delayObj1 = new DelayObj("bad",30);
                            delayQueue.put(delayObj);
                            delayQueue.put(delayObj1);
                            System.out.println(Thread.currentThread().getName() + "已经放了数据，" +"队列目前有" + delayQueue.size() + "个数据");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                    }
                }
            }.start();
        }
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        //将此处的睡眠时间分别改为100和1000，观察运行结果
                        Thread.sleep(1000);
                        System.out.println(Thread.currentThread().getName() + "准备取数据!");
                        Delayed take = (Delayed) delayQueue.take();
                        System.out.println(((DelayObj) take).getName());
                        System.out.println(Thread.currentThread().getName() + "已经取走数据》》" +take+ ",队列目前有" + delayQueue.size() + "个数据");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }.start();
        Thread.sleep(100000L);
    }


}



