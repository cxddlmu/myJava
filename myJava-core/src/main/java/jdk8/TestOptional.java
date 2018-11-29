package jdk8;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.*;

/**
 * Created by cuixiaodong on 2017/3/22.
 */
public class TestOptional {
    @Test
    public void test() {
        List<String> list = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put(null, null);
        map.put("1", "");
        map.put("", "");
        map.put("2", "TIME");
        map.put("3", "DATE");
        map.forEach((key, val) -> {
            Optional.ofNullable(key).ifPresent(list::add);
            Optional.ofNullable(val).flatMap(s -> Optional.of(s.toUpperCase()))
                    .flatMap(s -> Optional.of(s.toLowerCase())).filter(s -> s.equals("time")).ifPresent(list1::add);
        });
        list1.forEach(s -> Optional.ofNullable(s).map(s1 -> s1.substring(1, 2)).ifPresent(System.out::println));

        System.out.println(JSON.toJSON(list));
        System.out.println(JSON.toJSON(list1));
        Optional<String>
                name = Optional.of("Sanaulla");
        name.map(s -> s.toUpperCase()).ifPresent(System.out::println);
        Optional<Object> emptyVal = Optional.empty();
        String hellow = (String) Optional.ofNullable(null).orElseGet(() -> "hello");
        System.out.println(hellow);
        name.orElse("0");

        System.out.println(emptyVal.flatMap(Optional::ofNullable).orElse("is empty"));
//        System.out.println(nullVal.flatMap(Optional::ofNullable).orElse("is null"));

    }

}

class Obj {

}
