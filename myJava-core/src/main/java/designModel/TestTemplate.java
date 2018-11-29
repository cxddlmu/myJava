package designModel;

/**
 * Created by cuixiaodong on 2017/4/2.
 */
public class TestTemplate {
}
abstract class Template extends TemplateParent{
    abstract void method1();

    abstract void method2();

    public void exec() {
        method1();
        method2();
        method3();
    }

    @Override
    void method3() {
        System.out.println("Template.method3");
    }
}
abstract class TemplateParent{
    abstract void method3();
}
class TemplateImpl extends Template{

    @Override
    void method1() {
        System.out.println("TemplateImpl.method1");
    }

    @Override
    void method2() {
        System.out.println("TemplateImpl.method2");
    }
}
class TemplateImpl1 extends Template{
    @Override
    void method1() {
        System.out.println("TemplateImpl1.method1");
    }

    @Override
    void method2() {
        System.out.println("TemplateImpl1.method2");
    }
}