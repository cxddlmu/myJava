package lock;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.Exchanger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by cuixiaodong on 2017/3/27.
 */
public class TestExchange {
    ReentrantLock lock = new ReentrantLock();
    Condition conditionPrd = lock.newCondition();
    Condition conditionCus = lock.newCondition();
    LinkedList<String> stringList = new LinkedList<>();
    Exchanger exchanger = new Exchanger();

    public static void main(String[] args) throws InterruptedException {
        TestExchange testCondition = new TestExchange();

        new Thread(() -> {
            try {
                testCondition.set();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                testCondition.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                testCondition.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void set() throws InterruptedException {
        int i = 0;
        int j = 0;
        while (j < 10) {
            Thread.currentThread().sleep(1000L);
            System.out.println("set: wait to exchange");
            Object exchange = exchanger.exchange(stringList);
            System.out.println("set:what we got" + JSON.toJSON(exchange));
            String s = "可卡因" + i++;
            stringList.offer(s);
            System.out.println("set:" + s);
            j++;
        }
    }

    public void get() throws InterruptedException {
        while (true) {
            Thread.currentThread().sleep(1000L);
            System.out.println(Thread.currentThread().getName()+"get: prepare money,wait to exchange");
            Object money = exchanger.exchange(new ArrayList<String>(Arrays.asList("money")));
            System.out.println(Thread.currentThread().getName()+"get:what we got " + money);
        }

    }

}
