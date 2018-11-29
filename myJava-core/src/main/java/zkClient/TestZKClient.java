package zkClient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cuixiaodong on 2017/3/15.
 */
public class TestZKClient {
    private static final String ipAddr = "127.0.0.1";
    private static final int port = 2181;
    private static ZkClient zkClient= new ZkClient(ipAddr+":"+port);

    @Test
    public void testCreateNode() {
        System.out.println("conneted ok!");

        User user = new User();
        user.setId(1);
        user.setName("testUser");

        /**
         * "/testUserNode" :节点的地址
         * user：数据的对象
         * CreateMode.PERSISTENT：创建的节点类型
         */
        String path = zkClient.create("/testUserNode3/node", user, CreateMode.PERSISTENT);
        //输出创建节点的路径
        //System.out.println("created path:"+path);
        //System.out.println(zkClient.exists("/testUserNode"));
        Stat stat = new Stat();
        //获取 节点中的对象
        user = zkClient.readData("/testUserNode3",stat);
        user.setName("writeData");
        zkClient.writeData("/testUserNode3",user);
        System.out.println(user.getName());
        System.out.println(stat);

        /*//删除单独一个节点，返回true表示成功
        boolean e1 = zkClient.delete("/testUserNode");
        //删除含有子节点的节点
        boolean e2 = zkClient.deleteRecursive("/test");*/
    }

    @Test
    public void testSubscribe() throws InterruptedException {
        zkClient.subscribeChildChanges("/testUserNode3", new ZKListener());
        zkClient.subscribeDataChanges("/testUserNode3", new ZKListener());
        Thread.sleep(Integer.MAX_VALUE);
    }


    private static class ZKListener implements IZkChildListener,IZkDataListener {
        /**
         * handleChildChange： 用来处理服务器端发送过来的通知
         * parentPath：对应的父节点的路径
         * currentChilds：子节点的相对路径
         */
        public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
            System.out.println(parentPath);
            System.out.println(currentChilds.toString());
        }
        public void handleDataChange(String dataPath, Object data) throws Exception {
            System.out.println("handleDataChange: "+dataPath+":"+data.toString());
        }

        public void handleDataDeleted(String dataPath) throws Exception {
            System.out.println("handleDataDeleted: "+dataPath);
        }

    }
}
class User implements Serializable {

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}