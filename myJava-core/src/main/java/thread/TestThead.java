package thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by cuixiaodong on 2017/3/12.
 */
public class TestThead {
    private static ThreadLocal local = new ThreadLocal();
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        Thread thread = new Thread(() -> {
            System.out.println("gogogogo");
            try {
                while (true) {
                    if (Thread.interrupted()) {
                        if (Thread.interrupted()) {//false
                            System.out.println(111);
                        }
                        int ii=1/0;
                        System.out.println("done");
                        return;
                    }
                    System.out.println("good");
                }
            } catch (java.lang.Exception e) {
                e.printStackTrace();
            }
        });
        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("+++"+e.getMessage());
            }
        });
        thread.start();
        thread.interrupt();
    }

}

