package jdk8;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by cuixiaodong on 2017/3/21.
 */
public class TestLambda implements InterfaceA,InterfaceB{
    @Override
    public void methodA() {
        methodB();
    }

    @Override
    public void methodB() {
        new Comparable<Object>() {
            @Override
            public int compareTo(Object o) {
                return 0;
            }
        };
        new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return 0;
            }
        };
    }

    @Test
    public void testLambda() {
        Integer[] arr = {1, 2000, 3000, 4,100};
        String[] arrStr = {"1", "2", "0"};
        Arrays.sort(arrStr,String::compareToIgnoreCase);
        Arrays.sort(arr,Integer::compare);
        System.out.println(JSON.toJSON(arrStr));
        System.out.println(JSON.toJSON(arr));
        Arrays.asList(arr).forEach(System.out::println);
    }

    @Test
    public void testLambda1() {
        List<TestObj> testObjList = Lists.newArrayList(new TestObj(4),new TestObj(3),new TestObj(5));
        System.out.println(JSON.toJSONString(testObjList.stream().filter(testObj -> testObj.getAge() > 3)
                                                     .distinct().collect(Collectors.groupingBy(a -> a.getAge(), Collectors.summingInt(a -> 1)))));
        System.out.println(testObjList.stream().map(o -> o.getAge() * 0).reduce(0, Integer::sum));
        System.out.println(testObjList.stream().map(o -> o.getAge() * 1).reduce((sum, o) -> sum + o).get());
    }
}
@Setter
@Getter
class TestObj{
    private int age;

    public TestObj(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "TestObj{" +
                "age=" + age +
                '}';
    }
}
interface InterfaceA{
    void methodA();
    default void methodB() {

    }
}
interface InterfaceB{
    default void methodB() {
        System.out.println("methodB");
    }
}


