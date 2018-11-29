package collection;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by cuixiaodong on 2017/4/1.
 */
public class TestCollections {
    public static void main(String[] args) {

    }
    @Test
    public void Test(){
        List<Integer> integerList = Lists.newArrayList(1, 2, 3);
        List<Integer> integerList1 = Lists.newArrayList(4);
        Assert.assertTrue(Collections.disjoint(integerList1, integerList));
        Assert.assertFalse(Collections.disjoint(Lists.newArrayList(1, 2, 3), integerList));
        System.out.println(Collections.checkedList(integerList, int.class));
        Assert.assertEquals(Collections.frequency(integerList, 1),1);
        Collections.swap(integerList,1,2);
        System.out.println(JSON.toJSON(integerList));
        Collections.fill(integerList,1);
        System.out.println(JSON.toJSON(integerList));
        System.out.println(Collections.nCopies(4, Maps.newHashMap()));
        Assert.assertEquals(3,(long)Collections.max(Lists.newArrayList(1, 2, 3), new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2>o1?o2:o1;
            }
        }));
        ConcurrentMap<Object, Boolean> objectObjectConcurrentMap = Maps.newConcurrentMap();
        Set<Object> objectSet = Collections.newSetFromMap(objectObjectConcurrentMap);
        objectSet.add("java");
        objectSet.add("safe");
        System.out.println(JSON.toJSON(objectObjectConcurrentMap));
        List<Integer> integerList2 = Collections.unmodifiableList(Lists.newArrayList(1, 2, 3));
//        Assert.assertFalse(integerList2.add(9));异常
        Collections.singletonList("3");
        ArrayList<Integer> newArrayList = Lists.newArrayList(1, 2, 3);
        Collections.replaceAll(newArrayList, 1, 10);
        System.out.println(JSON.toJSON(newArrayList));
    }
}
enum TestEnum{
    one,two,three
}
