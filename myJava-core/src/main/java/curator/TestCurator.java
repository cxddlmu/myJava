package curator;

import com.alibaba.fastjson.JSON;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuixiaodong on 2017/3/17.
 */
public class TestCurator {
    /** Zookeeper info */
    private static final String ZK_ADDRESS = "127.0.0.1:2181";
    private static final String ZK_PATH = "/zktest";
    private static final CuratorFramework client = CuratorFrameworkFactory.newClient(
            ZK_ADDRESS,
            new RetryNTimes(10, 5000)
    );
    @Test
    public void init() throws Exception {
        // 1.Connect to zk
        client.start();
        // 注册观察者，当节点变动时触发
        client.getData().usingWatcher(new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("node is changed");
            }
        }).inBackground().forPath("/znode1");
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void test() throws Exception {
        ACLProvider aclProvider = new ACLProvider() {
            private List<ACL> acl ;
            @Override
            public List<ACL> getDefaultAcl() {
                if(acl ==null){
                    ArrayList<ACL> acl = ZooDefs.Ids.CREATOR_ALL_ACL;
                    acl.clear();
                    acl.add(new ACL(ZooDefs.Perms.ALL, new Id("auth", "admin:admin") ));
                    this.acl = acl;
                }
                return acl;
            }
            @Override
            public List<ACL> getAclForPath(String path) {
                return acl;
            }
        };

        String scheme = "digest";
        byte[] auth = "admin:admin".getBytes();
        int connectionTimeoutMs = 5000;
        String connectString = "127.0.0.1:2181";
        String namespace = "testnamespace";
        CuratorFramework client = CuratorFrameworkFactory.builder().aclProvider(aclProvider).
                authorization(scheme, auth).
                connectionTimeoutMs(connectionTimeoutMs).
                connectString(connectString).
                namespace(namespace).
                retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000)).build();
        client.start();
        String path="/test";
        Stat stat = client.checkExists().forPath(path);
        if(stat==null){
            System.out.println("exec create path:"+path);
            client.create().forPath("/test");
        }else {
            System.out.println("exec getData");
        }
        /*client.create().forPath("/test/1");
        client.create().forPath("/test/2");*/
        client.setData().forPath("/test/1","good".getBytes());
        List<String> test = client.getChildren().forPath("/test");
        System.out.println(JSON.toJSON(test));
        System.out.println(new String(client.getData().forPath("/test")));

    }


}
