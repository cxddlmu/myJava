package curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;

import java.util.concurrent.TimeUnit;

/**
 * Created by cuixiaodong on 2017/3/28.
 */
public class ExampleClientThatLocks {
    private final InterProcessMutex lock;
    private final InterProcessSemaphoreMutex lock1;
    private final FakeLimitedResource resource;
    private final String clientName;

    public ExampleClientThatLocks(CuratorFramework client, String lockPath, FakeLimitedResource resource, String clientName) {
        this.resource = resource;
        this.clientName = clientName;
        lock = new InterProcessMutex(client, lockPath);
        lock1 = new InterProcessSemaphoreMutex(client, lockPath);
    }

    public void doWork(long time, TimeUnit unit) throws Exception {
        if (!lock.acquire(time, unit)) {
            throw new IllegalStateException(clientName + " could not acquire the lock");
        }
        try {
            System.out.println(clientName + " has the lock");
            resource.use(); //access resource exclusively
        } finally {
            System.out.println(clientName + " releasing the lock");
            lock.release(); // always release the lock in a finally block
        }
    }
    public void doWork(long time, TimeUnit unit,String nonReentry) throws Exception {
        if (!lock1.acquire(time, unit)) {
            throw new IllegalStateException(clientName + " could not acquire the lock");
        }
        System.out.println(clientName + " has the lock");
        if (!lock1.acquire(time, unit)) {
            throw new IllegalStateException(clientName + " could not acquire the lock");
        }
        System.out.println(clientName + " has the lock again");

        try {
            resource.use(); //access resource exclusively
        } finally {
            System.out.println(clientName + " releasing the lock");
            lock1.release(); // always release the lock in a finally block
            lock1.release(); // always release the lock in a finally block
        }
    }
}