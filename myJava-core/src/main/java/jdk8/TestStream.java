package jdk8;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by cuixiaodong on 2017/3/23.
 */
public class TestStream {
    @Test
    public void test() {
        Map<String, String> map = new HashMap<>();
        map.put(null, null);
        map.put("1", "");
        map.put("", "");
        map.put("2", "TIME");
        map.put("3", "DATE");
        List<String> list = new ArrayList<>();
        List<String> words = Arrays.asList("Java 8", "Lambdas", "In", "Action","hello","world");
        System.out.println(JSON.toJSON(words.stream().map(s -> s.split("")).distinct().collect(Collectors.toList())));
        System.out.println(words.stream().collect(Collectors.joining(",")));
        System.out.println(words.stream().collect(Collectors.summingInt(s ->1)));
        map.forEach((key,val) ->{list.add(key);list.add(val); });
        list.stream().limit(3).filter(s -> Integer.parseInt(s)>1).distinct().skip(1);
    }

    @Test
    /**
     * function predict consumer
     */
    public void testStream() {
        List<User> userList = Lists.newArrayList(
                new User("cui",30,170D,65D,Boolean.FALSE)
                ,new User("xu",25,165D,50D,Boolean.FALSE)
                ,new User("rong",30,177D,75D,Boolean.FALSE));
        Map<Integer, List<User>> integerListMap = userList.stream().collect(Collectors.groupingBy(User::getAge));
//        System.out.println(userList.stream().collect(Collectors.toList()));
//        System.out.println(userList.stream().collect(Collectors.toSet()));
        System.out.println(userList.stream().collect(Collectors.counting()));
        System.out.println(JSON.toJSON(userList.stream().collect(Collectors.minBy(Comparator.comparingInt(User::getAge))).orElse(new User())));
        System.out.println(JSON.toJSON(userList.stream().collect(Collectors.summingInt(User::getAge))));
        System.out.println(JSON.toJSON(userList.stream().collect(Collectors.reducing(0,User::getAge,Integer::sum))));
        System.out.println(JSON.toJSON(userList.stream().collect(Collectors.summarizingInt(User::getAge))));
        System.out.println(JSON.toJSON(userList.stream().collect(Collectors.groupingBy(User::getAge,Collectors.counting()))));
        System.out.println(JSON.toJSON(userList.stream().collect(Collectors.groupingBy(User::getAge,Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingDouble(User::getHeight)),Optional::get)))));
        System.out.println(JSON.toJSON(userList.stream().collect(Collectors.groupingBy(User::getAge,Collectors.maxBy(Comparator.comparingDouble(User::getHeight))))));
        System.out.println(JSON.toJSON(userList.stream().collect(Collectors.groupingBy(User::getAge,Collectors.groupingBy(User::getWeight)))));
        System.out.println(JSON.toJSON(userList.stream().map(User::getName).collect(Collectors.joining(","))));
        System.out.println(JSON.toJSON(userList.stream().mapToInt(User::getAge).sum()));
        System.out.println(JSON.toJSON(userList.stream().collect(Collectors.partitioningBy(User::getMarried,Collectors.summarizingDouble(User::getHeight)))));
        System.out.println(JSON.toJSON(integerListMap));
        System.out.println("IntStream.rangeClosed(1,100) = " + IntStream.rangeClosed(1,100));
        List<User> dishes = userList.stream().collect( ArrayList::new, List::add, List::addAll);//重载版本
        System.out.println(JSON.toJSON(dishes));
        System.out.println(filter(userList, (s) -> s.getAge() > 25));

    }

    public static <T> List<T> filter(List<T> list, Predicate<T> p){
        List<T> result = new ArrayList<>();
        for(T e: list){
            if(p.test(e)){
                result.add(e);
            }
        }
        return result;
    }
}

@Setter
@Getter
class User{
    private String name;
    private Integer age;
    private Double weight;
    private Double height;
    private Boolean married;

    public User() {
    }

    public User(String name, Integer age, Double weight, Double height,Boolean married) {
        this.weight=weight;
        this.height=height;
        this.name=name;
        this.age=age;
        this.married=married;

    }
}
