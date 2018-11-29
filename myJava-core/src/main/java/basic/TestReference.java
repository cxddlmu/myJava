package basic;

import org.junit.Test;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * Created by cuixiaodong on 2017/3/10.
 */
public class TestReference {
    public static void main(String[] args) {

    }

    @Test
    public void testWeak() throws InterruptedException {
        Car obj = new Car("red");
        WeakReference<Car> weakCar = new WeakReference<Car>(obj);
        obj=new Car("blue");
        int i= 0;
        while(true){
            String[] arr = new String[1000];
            if(weakCar.get()!=null){
//                Thread.currentThread().sleep(1000);
                // do something
                System.out.println(i++);
            }else{
                System.out.println("Object has been collected.");
                break;
            }
        }
    }

    @Test
    public void testSoft() {
        Car obj = new Car("red");
        SoftReference<Car> softCar = new SoftReference<Car>(obj);
        //obj=new Car("blue");
        int i=0;
        while(true){
            String[] arr = new String[1000];
            if(softCar.get()!=null){
                // do something
                System.out.println(i++);
            }else{
                System.out.println("Object has been collected.");
                break;
            }
        }
    }
}

class Car{
    private String name;
    public Car(String name) {
        this.name=name;
    }
}
