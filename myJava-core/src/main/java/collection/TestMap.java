package collection;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by cuixiaodong on 2017/3/4.
 */
public class TestMap {
    public static void main(String[] args) throws InterruptedException {
        testIdentityHashMap();
//        testLinkedHashMap();
        testHashMap();
        testTreeMap();
        ConcurrentMap<Object, Object> objectObjectConcurrentMap = Maps.newConcurrentMap();
        objectObjectConcurrentMap.put(1, 1);
        WeakHashMap weakHashMap = new WeakHashMap();
        weakHashMap.put(1, 1);
        Thread.currentThread().sleep(3000);
        System.out.println("JSON.toJSONString(weakHashMap) = " + JSON.toJSONString(weakHashMap));
        weakHashMap.get(1);
        System.out.println("JSON.toJSONString(weakHashMap) = " + JSON.toJSONString(weakHashMap));
        ConcurrentSkipListMap concurrentSkipListMap = new ConcurrentSkipListMap();
    }

    private static void testIdentityHashMap() {
        java.util.IdentityHashMap identityHashMap = Maps.newIdentityHashMap();
        TestBean testBean = new TestBean(1, "2");
        TestBean testBean1 = new TestBean(1, "2");
        identityHashMap.put(testBean, "key1Value");
        identityHashMap.put(testBean, "key2Value");
        identityHashMap.put(testBean1, "key2Val");
        identityHashMap.put(null, null);
        identityHashMap.put(null, "nullVal");
        identityHashMap.put("", "nullVal");
        identityHashMap.put(" ", "nullVal");
        identityHashMap.put("中国", "日本");
        identityHashMap.put("中国", "中国");
        System.out.println(identityHashMap.put(new String("44"),44));
        System.out.println(identityHashMap.put(new String("44"),55));
        Map hashMap = Maps.newHashMap();
        Map hashMap1 = Maps.newHashMap();
        hashMap.put(1, 1);
        hashMap1=hashMap;
        identityHashMap.put(hashMap,66);
        identityHashMap.put(hashMap1,77);
        System.out.println(identityHashMap.entrySet());
    }
    @Test
    public void testLinkedHashMap() {
        LinkedHashMap linkedHashMap = new LinkedHashMap(10,0.75f,true);
        linkedHashMap.put(1, 1);
        linkedHashMap.put(2, 2);
        linkedHashMap.put("", null);
        linkedHashMap.put(" ", null);
        linkedHashMap.put(null, null);
        Object o1 = linkedHashMap.putIfAbsent(1, 1333);
        Object o = linkedHashMap.putIfAbsent("", 111);
        Object o2 = linkedHashMap.putIfAbsent(" ", 222);
        System.out.println(linkedHashMap.get(""));
        System.out.println(linkedHashMap.get(" "));
        System.out.println(linkedHashMap.entrySet());
    }

    private static void testHashMap() {
        Map hashMap = Maps.newHashMap();
        Map hashMap1 = Maps.newHashMap();
        Map hashMap2 = Maps.newHashMap();
        Map<String,String> hashMap3 = Maps.newHashMap();
        hashMap2.put(4, 4);
        hashMap.put(1,hashMap2);
        hashMap1=hashMap;
        hashMap.put(2, 2);
        System.out.println(hashMap1);
        hashMap1.put(3, 3);
        System.out.println(hashMap);
        hashMap2.put(5, 5);
        hashMap3.put("", "");
        hashMap3.put(" ", "33");
        System.out.println("hashMap3 = " + hashMap3);
    }

    private static void testTreeMap() {
        TreeMap treeMap = Maps.newTreeMap();
        treeMap.put(4, 4);
        treeMap.put(1, 1);
        treeMap.put(10, 10);
        System.out.println("JSON.toJSONString(treeMap) = " + JSON.toJSONString(treeMap));
        System.out.println("treeMap.higherKey(4) = " + treeMap.higherKey(4));

    }

    @Test
    public void testWeakHashMap() {
        WeakHashMap weakHashMap = new WeakHashMap();
        weakHashMap.put(null,null);
    }
}
