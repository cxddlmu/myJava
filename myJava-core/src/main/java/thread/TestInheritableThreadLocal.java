package thread;

/**
 * Created by cuixiaodong on 2017/3/27.
 */

import static java.lang.System.out;

public class TestInheritableThreadLocal {
    public static void main(String[] args) {
//        new TestInheritableThreadLocal().testInheritableThreadLocal();
        new TestInheritableThreadLocal().testThreadLocal();
    }
    public void testThreadLocal() {
        final ThreadLocal<String> local = new ThreadLocal<String>();
        work(local);
    }

    public void testInheritableThreadLocal() {
        final ThreadLocal<String> local = new InheritableThreadLocal<String>();
        work(local);
    }

    private void work(final ThreadLocal<String> local) {
        local.set("a");
        out.println(Thread.currentThread() + "," + local.get());
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                out.println(Thread.currentThread() + "," + local.get());
                local.set("b");
                out.println(Thread.currentThread() + "," + local.get());
            }
        });

        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        out.println(Thread.currentThread() + "," + local.get());
    }
}
