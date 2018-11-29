package thread;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * Created by cuixiaodong on 2017/3/23.
 */
public class TestForkJoin {
    @org.junit.Test
    public void test1() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        //生成一个计算资格，负责计算1+2+3+4
        CountTask task = new CountTask(1, 4);
        Future<Integer> result = forkJoinPool.submit(task);//异步
        Object result1 = forkJoinPool.invoke(task);//同步
        try {
            System.out.println("now end");
            System.out.println(result1);
        } catch (Exception e) {
        }
    }


}
class CountTask extends RecursiveTask<java.lang.Integer> {
    private static final int THRESHOLD = 2;

    private int start;

    private int end;

    public CountTask(int start, int end) {
        this.start = start;
        this.end = end;
    }


    @Override
    protected java.lang.Integer compute() {
        int sum = 0;
        //System.out.println("log");
        boolean canCompute = (end - start) <= THRESHOLD;
        if (canCompute) {
            for (int i = start; i <= end; i++)
                sum += i;
        } else {
            //如果任务大于阀值，就分裂成两个子任务计算
            int mid = (start + end) / 2;
            CountTask leftTask = new CountTask(start, mid);
            CountTask rightTask = new CountTask(mid+1, end);
            try {
                Thread.currentThread().sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //执行子任务
            leftTask.fork();
            rightTask.fork();

            //等待子任务执行完，并得到结果
            int leftResult = (int)leftTask.join();
            int rightResult = (int)rightTask.join();

            sum = leftResult + rightResult;
        }

        return sum;
    }
}
