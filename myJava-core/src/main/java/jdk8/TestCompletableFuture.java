package jdk8;

import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by cuixiaodong on 2017/3/24.
 */
public class TestCompletableFuture {
    public void test() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        CompletableFuture<String> resultCompletableFuture = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return "hello";
            }
        },executor);
        resultCompletableFuture.thenAcceptAsync(new Consumer<String>() {//ForkJoinPool.commonPool
            @Override
            public void accept(String t) {
                System.out.println(t);
                System.out.println(Thread.currentThread().getName());
            }
        });
        System.out.println(123);
    }

    public void test1() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            return "zero";
        }, executor);

        CompletableFuture<Integer> f2 = f1.thenApply(new Function<String, Integer>() {

            @Override
            public Integer apply(String t) {
                System.out.println(2);
                return Integer.valueOf(t.length());
            }
        });

        CompletableFuture<Double> f3 = f2.thenApply(r -> r * 2.0);
        System.out.println(f3.get());
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new TestCompletableFuture().test();
    }
}

