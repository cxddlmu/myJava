package designModel;

/**
 * Created by cuixiaodong on 2017/4/2.
 */
public class TestDecorator {

    public static void main(String[] args) {
        Decorator decorator = new DecoratorImpl1(new DecoratorImpl(new source()));
        decorator.method();
    }
}
interface Decorator{
    void method();
}
class DecoratorImpl implements Decorator{
    private Decorator decorator;

    public DecoratorImpl(Decorator decorator) {
        super();
        this.decorator = decorator;
    }

    @Override
    public void method() {
        decorator.method();
        System.out.println("DecoratorImpl.method");
    }
}

class DecoratorImpl1 implements Decorator{
    private Decorator decorator;

    public DecoratorImpl1(Decorator decorator) {
        super();

        this.decorator = decorator;
    }

    @Override
    public void method() {
        decorator.method();
        System.out.println("DecoratorImpl1.method");
    }
}

class source implements Decorator{

    @Override
    public void method() {
        System.out.println("source.method");
    }
}


