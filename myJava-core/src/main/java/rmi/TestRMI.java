package rmi;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by cuixiaodong on 2017/3/25.
 */
public class TestRMI {
    public static void main(String[] args) {
        String url = "rmi://localhost:8888/";
        try {
// 在RMI服务注册表中查找名称为service02的对象，并调用其上的方法
            IService service02 = (IService) Naming.lookup(url + "service03");
            Class stubClass = service02.getClass();
            System.out.println(service02 + " 是 " + stubClass.getName() + " 的实例！");
// 获得本底存根已实现的接口类型
            Class[] interfaces = stubClass.getInterfaces();
            for (Class c : interfaces) {
                System.out.println("存根类实现了 " + c.getName() + " 接口！");
            }
            System.out.println(service02.service("你好！"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

interface IService extends Remote {
    //声明服务器端必须提供的服务
    String service(String content) throws RemoteException;
}

class ServiceImpl extends UnicastRemoteObject implements IService {
    private String name;

    public ServiceImpl(String name) throws RemoteException {
        this.name = name;
    }

    @Override
    public String service(String content) {
        return "server >> " +name+ content;
    }
}

class Server {
    public static void main(String[] args) {
        try {
// 实例化实现了IService接口的远程服务ServiceImpl对象
            IService service02 = new ServiceImpl("service02");
            IService service03 = new ServiceImpl("service03");
// 本地主机上的远程对象注册表Registry的实例，并指定端口为8888，这一步必不可少（Java默认端口是1099），必不可缺的一步，缺少注册表创建，则无法绑定对象到远程注册表上
            LocateRegistry.createRegistry(8888);
// 把远程对象注册到RMI注册服务器上，并命名为service02
//绑定的URL标准格式为：rmi://host:port/name(其中协议名可以省略，下面两种写法都是正确的）
            Naming.bind("rmi://localhost:8888/service02", service02);
            Naming.bind("rmi://localhost:8888/service03", service03);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("服务器向命名表注册了1个远程服务对象！");
    }
}