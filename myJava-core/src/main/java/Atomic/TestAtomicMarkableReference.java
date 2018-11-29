package Atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * Created by cuixiaodong on 2017/3/28.
 */
public class TestAtomicMarkableReference {
    private static AtomicInteger atomicInt = new AtomicInteger(100);

    private static AtomicMarkableReference atomicStampedRef = new AtomicMarkableReference(100, false);


    public static void main(String[] args) throws InterruptedException {

        Thread intT1 = new Thread(new Runnable() {

            @Override

            public void run() {

                atomicInt.compareAndSet(100, 101);

                atomicInt.compareAndSet(101, 100);

            }

        });


        Thread intT2 = new Thread(new Runnable() {

            @Override

            public void run() {

                try {

                    TimeUnit.SECONDS.sleep(1);

                } catch (InterruptedException e) {

                }

                boolean c3 = atomicInt.compareAndSet(100, 101);

                System.out.println(c3); // true

            }

        });


        intT1.start();

        intT2.start();

        intT1.join();

        intT2.join();


        Thread refT1 = new Thread(new Runnable() {

            @Override

            public void run(){

                try

                {

                    TimeUnit.SECONDS.sleep(1);

                } catch(
                        InterruptedException e)

                {

                }

                atomicStampedRef.compareAndSet("abc","bcd",false,true);

                atomicStampedRef.compareAndSet(101,100,true,false);

            }

        });


        Thread refT2 = new Thread(new Runnable() {

            @Override

            public void run() {
                boolean stamp = atomicStampedRef.isMarked();

                try {

                    TimeUnit.SECONDS.sleep(2);

                } catch (InterruptedException e) {

                }

                atomicStampedRef.set(101,!stamp);
                atomicStampedRef.attemptMark(100, true);

                System.out.println(atomicStampedRef.getReference()); // false

            }

        });



        refT1.start();

        refT2.start();

    }
}
