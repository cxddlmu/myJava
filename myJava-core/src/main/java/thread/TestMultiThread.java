package thread;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by cuixiaodong on 2017/3/23.
 */
enum ExecutorsEnum {
    NewFixedThreadPool("java.util.concurrent.Executors"),
    NewScheduledThreadPool(""),
    NewWorkStealingPool(""),
    NewCachedThreadPool("");

    private ExecutorsEnum(String classPath) {
        this.classPath = classPath;
    }

    public String getClassPath() {
        return classPath;
    }

    private String classPath;

}

public class TestMultiThread {
    private static ThreadLocal local = new ThreadLocal();
    private static long localInt = 0;

    static void testNewWorkStealingPool() {
        ForkJoinPool executorService = (ForkJoinPool) Executors.newWorkStealingPool(1);

        //生成一个计算资格，负责计算1+2+3+4
        CountTask task = new CountTask(1, 4);
        ForkJoinTask<Integer> forkJoinTask = executorService.submit(task);//异步
//Object result1 = executorService.invoke(task);//同步
        try {
            System.out.println("now end");
            System.out.println(forkJoinTask.get());
        } catch (Exception e) {
        }
    }

    static void testNewCachedThreadPool() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r) ;
            }
        });
        while (true) {
            Future<Object> objectFuture = executorService.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return "good";
                }
            });
            System.out.println(objectFuture.get());
            Future<Object> future = executorService.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println();
                }
            },"redefined result");//预定义结果result
            System.out.println(objectFuture.get());
        }

    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
//        new TestMultiThread().testFutureException();
//        new TestMultiThread().testFuture();
//        Executors.newCachedThreadPool();
//        TestMultiThread.testNewCachedThreadPool();
        TestMultiThread.testNewWorkStealingPool();
    }

    ExecutorService executorService = Executors.newFixedThreadPool(5);
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
    TraceThreadPoolExecutor traceThreadPoolExecutor = new TraceThreadPoolExecutor(5, 5, 0, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(5));
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 5, 0, TimeUnit.MILLISECONDS
            , new LinkedBlockingDeque<Runnable>(5), Executors.defaultThreadFactory()
            , new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        System.out.println("rejectedExecution");
                    }
                })
    {
        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            System.out.println("TestThread.beforeExecute" + t.getName());
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            System.out.println("TestThread.afterExecute" + Thread.currentThread().getName());
        }

        @Override
        protected void terminated() {
            System.out.println("terminated");
        }

    };

    @org.junit.Test
    public void testFixedRate() {
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    @org.junit.Test
    public void testFixedDelay() {
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(System.currentTimeMillis() + " Thread.currentThread().getName() = " + Thread.currentThread().getName());
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    @org.junit.Test
    public void testNormal() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.printf("%s \n", Thread.currentThread().getName());
            }
        });
    }

    public void testFutureException() throws ExecutionException, InterruptedException {
        for (int i = 0; i < 10; i++) {
            Future<?> submit = traceThreadPoolExecutor.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    //Thread.sleep(5000);
                    //int j=1/0;
                    int i1 = 0;
                    int i2 = 1 / i1;
                    System.out.println(Thread.currentThread().getName());
                    return System.currentTimeMillis() + " " + Thread.currentThread().getName();
                }
            });
            submit.get();
        }
    }

    public void testFuture() throws ExecutionException, InterruptedException, TimeoutException {
        for (int i = 0; i < 100; i++) {
            Future<?> submit = threadPoolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        local.set(Thread.currentThread().getId() + 1);
                        localInt = Thread.currentThread().getId() + 1;
                        int i1 = 0;
                        int i2 = 1 / i1;
                        System.out.println(Thread.currentThread().getName() + " local " + local.get());
                        System.out.println(Thread.currentThread().getName() + " localInt " + localInt);
                        //int j=1/0;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        //System.out.println("future "+submit.get(1000,TimeUnit.MILLISECONDS));//会阻塞

        Future<?> submit1 = threadPoolExecutor.submit(new Callable<Object>() {
            @Override
            public String call() {
                try {
                    local.set(Thread.currentThread().getId() + 1);
                    localInt = Thread.currentThread().getId() + 1;
                    Thread.sleep(3000);
                    System.out.println(Thread.currentThread().getName() + "  " + local.get());
                    System.out.println(Thread.currentThread().getName() + "  " + localInt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "good";
            }
        });
        System.out.println("now end");
        System.out.println("future " + submit1.get());//会阻塞
        threadPoolExecutor.shutdown();

    }

    @org.junit.Test
    public void testFuture2() throws InterruptedException, ExecutionException {
        List<Future<Integer>> futures = threadPoolExecutor.invokeAll(Arrays.<Callable<Integer>>asList(new Callable() {
            @Override
            public Integer call() {
                try {
                    local.set(1000);
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("first");
                return (Integer) local.get();
            }
        }, new Callable() {
            @Override
            public Integer call() {
                System.out.println("second");
                return 2;

            }
        }));
        System.out.println(futures.get(0).get());
        System.out.println(futures.get(1).get());
    }

    @org.junit.Test
    public void testCountDownLatch() {
//启动会议室线程，等待与会人员参加会议
        Conference conference = new Conference(3);
        new Thread(conference).start();

        for (int i = 0; i < 3; i++) {
            Participater participater = new Participater("chenssy-0" + i, conference);
            Thread thread = new Thread(participater);
            thread.start();
        }
    }
}

class Participater implements Runnable {
    private String name;
    private Conference conference;

    public Participater(String name, Conference conference) {
        this.name = name;
        this.conference = conference;
    }

    @Override
    public void run() {
        conference.arrive(name);
    }
}

class Conference implements Runnable {
    private final CountDownLatch countDown;

    public Conference(int count) {
        countDown = new CountDownLatch(count);
    }

    /**
     * 与会人员到达，调用arrive方法，到达一个CountDownLatch调用countDown方法，锁计数器-1
     *
     * @param name
     * @author:chenssy
     * @data:2015年9月6日
     */
    public void arrive(String name) {
        System.out.println(name + "到达.....");
        //调用countDown()锁计数器 - 1
        countDown.countDown();
        System.out.println("还有 " + countDown.getCount() + "没有到达...");
    }

    @Override
    public void run() {
        System.out.println("准备开会，参加会议人员总数为：" + countDown.getCount());
        //调用await()等待所有的与会人员到达
        try {
            countDown.await();
        } catch (InterruptedException e) {
        }
        System.out.println("所有人员已经到达，会议开始.....");
    }
}

class TraceThreadPoolExecutor extends ThreadPoolExecutor {
    public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(wrap(task, trace()));
    }

    private <T> Runnable wrap(final Runnable task, Exception trace) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    task.run();
                } catch (Exception e) {
                    trace.printStackTrace();
                    throw e;
                }
            }
        };
    }

    private Exception trace() {
        return new Exception("exception trace");
    }
}