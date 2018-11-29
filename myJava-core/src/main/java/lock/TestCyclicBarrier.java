package lock;

/**
 * Created by cuixiaodong on 2017/3/27.
 */
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TestCyclicBarrier {
    final CyclicBarrier barrier;
    final int MAX_TASK;
    public TestCyclicBarrier(int cnt) {
        barrier = new CyclicBarrier(cnt + 1);
        MAX_TASK = cnt;
    }
    public void doWork(final Runnable work) {
        new Thread() {
            public void run() {
                work.run();
                try {
                    System.out.println(barrier.getNumberWaiting());
                    int index = barrier.await(1000L, TimeUnit.MILLISECONDS);//多个一个线程
                    doWithIndex(index);
                } catch (InterruptedException e) {
                    return;
                } catch (BrokenBarrierException e) {
                    return;
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private void doWithIndex(int index) {
        if (index == MAX_TASK / 3) {
            System.out.println("Left 30%.");
        } else if (index == MAX_TASK / 2) {
            System.out.println("Left 50%");
        } else if (index == 0) {
            System.out.println("run over");
        }
    }
    public void waitForNext() {
        try {
            doWithIndex(barrier.await());
        } catch (InterruptedException e) {
            return;
        } catch (BrokenBarrierException e) {
            return;
        }
    }
    public static void main(String[] args) {
        final int count = 2;
        TestCyclicBarrier demo = new TestCyclicBarrier(count);
        for (int i = 0; i < 10 ;i++){
            demo.doWork(new Runnable() {
                public void run() {
                    //do something
                    try {
                        Thread.sleep(1000L);
                    } catch (Exception e) {
                        return;
                    }
                }
            });
            /*if ((i + 1) % count == 2) {
                demo.waitForNext();
            }*/
        }
    }
}
