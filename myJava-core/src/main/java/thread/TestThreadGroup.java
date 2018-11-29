package thread;

/**
 * Created by cuixiaodong on 2017/3/27.
 */
public class TestThreadGroup {
    public static void main(String[] args) {
        Thread thread = new Thread(new ThreadGroup("group"),"thread"){
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread1 = new Thread(new ThreadGroup("group"),"thread1"){
            @Override
            public void run() {
                throw new RuntimeException();
            }
        };
        thread.start();
        thread1.start();
    }
}
