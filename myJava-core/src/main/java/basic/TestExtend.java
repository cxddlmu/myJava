package basic;

import org.junit.Test;

/**
 * Created by cuixiaodong on 2017/3/21.
 */
public class TestExtend {
    @Test
    public void test() {


    }
}

class Parent<A>{
    Object methodA(A obj) {
        return 2;
    }
}
class Parent1{
    Object methodA(Object obj) {
        return 2;
    }
}
class child extends Parent1{
    @Override
    String methodA(Object obj) {
        return "1";
    }
}

