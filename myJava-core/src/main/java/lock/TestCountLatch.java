package lock;

import java.util.concurrent.CountDownLatch;

/**
 * Created by cuixiaodong on 2017/3/27.
 */
public class TestCountLatch {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(5);
        CountDownLatch latch1 = new CountDownLatch(1);
        for(int i=0;i<5;i++){
            new Thread(new readNum(i,latch,latch1)).start();
        }
        Thread.currentThread().sleep(2000);
        latch1.countDown();
        latch.await();
        System.out.println("线程执行结束。。。。");
    }
    static class readNum  implements Runnable{
        private int id;
        private CountDownLatch latch;
        private CountDownLatch latch1;
        public readNum(int id,CountDownLatch latch,CountDownLatch latch1){
            this.id = id;
            this.latch = latch;
            this.latch1 = latch1;
        }
        @Override
        public void run() {
            synchronized (this){
                System.out.println("id:"+id);
                try {
                    latch1.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    latch.countDown();
                }
                System.out.println("线程组任务"+id+"结束，其他任务继续");
            }
        }
    }
}

