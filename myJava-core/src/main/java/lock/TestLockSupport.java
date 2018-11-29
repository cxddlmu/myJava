package lock;


import java.util.concurrent.locks.LockSupport;

/**
 * Created by cuixiaodong on 2017/3/27.
 */
public class TestLockSupport {

        public static void main(String[] args) throws InterruptedException {

            Thread thread = new Thread(() -> {
                LockSupport.park();
                System.out.println("gogogo");
            });
            thread.start();
            Thread.currentThread().sleep(3000L);
            new Thread(() ->LockSupport.unpark(thread)).start();

        }


}
