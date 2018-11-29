package cgLib;


import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by cuixiaodong on 2017/3/7.
 */
public class TestCgLib {
    public static void main(String[] args) {
        interfaceA proxyClass = (interfaceA)new ProxyClass().getInstance(new Original());
        proxyClass.methodA();
        //
        Original bookCglib=(Original)new ProxyClassCgLib().getInstance(new Original());
        bookCglib.methodA();
    }
}
interface interfaceA{
    void methodA();
}
class Original implements interfaceA{
    @Override
    public void methodA() {
        System.out.println("FFF");
    }
}
class ProxyClass implements InvocationHandler{
    private Object target;
    public Object getInstance(Object target) {
        this.target=target;
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(), this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("begin");
        method.invoke(target,args);
        return null;
    }
}


class ProxyClassCgLib implements MethodInterceptor {
    private Object target;
    public Object getInstance(Object target) {
        this.target = target;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.target.getClass());// 回调方法
        enhancer.setCallback(this);// 创建代理对象
        return enhancer.create();
    }
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("begin");
        methodProxy.invokeSuper(o,objects);
        return null;
    }

}
