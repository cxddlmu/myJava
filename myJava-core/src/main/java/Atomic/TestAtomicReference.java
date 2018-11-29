package Atomic;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by cuixiaodong on 2017/3/28.
 */
@Getter
public class TestAtomicReference {
    public static void main(String[] args) {
        SpinLock spinLock = new TestAtomicReference().new SpinLock();
        new Thread(new t1(spinLock)).start();
        //new Thread(new t2(spinLock)).start();

    }
    class SpinLock{
        private AtomicReference<Thread> owner =new AtomicReference<>();
        public void lock() throws InterruptedException {
            Thread current = Thread.currentThread();
            boolean b = owner.compareAndSet(null, current);
            System.out.println(b);
            while(!b){

            }
        }

        public void unlock () throws InterruptedException {
            System.out.println("ffffffffff");
            Thread current = Thread.currentThread();
            owner.compareAndSet(current, null);
        }


    }
}
class t1 implements Runnable{
    private TestAtomicReference.SpinLock spinLock;
    public t1(TestAtomicReference.SpinLock spinLock) {
        this.spinLock=spinLock;
    }

    @Override
    public void run() {
        try {
            spinLock.lock();
            spinLock.lock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class t2 implements Runnable{
    private TestAtomicReference.SpinLock spinLock;
    public t2(TestAtomicReference.SpinLock spinLock) {
        this.spinLock=spinLock;
    }

    @Override
    public void run() {
        try {
            spinLock.unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}