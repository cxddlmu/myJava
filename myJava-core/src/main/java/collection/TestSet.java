package collection;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by cuixiaodong on 2017/3/5.
 */
public class TestSet {
    public static void main(String[] args) {
        testHashMap();
    }

    private static void testHashMap() {
        HashSet<Object> hashSet = Sets.newHashSet();
        hashSet.add(1);
        hashSet.add(null);
        hashSet.add(null);
        System.out.println("JSON.toJSONString(hashSet) = " + JSON.toJSONString(hashSet));
        Sets.newLinkedHashSet();
        Sets.newIdentityHashSet();
        Sets.newTreeSet();
        Sets.newCopyOnWriteArraySet();//list
    }

    public void testEnumSet() {

    }

    private static void testCopyOnWriteArraySet() {
        CopyOnWriteArraySet copyOnWriteArraySet = new CopyOnWriteArraySet();
        copyOnWriteArraySet.add(null);
    }
    @Test
    public void testLinkedHashSet() {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        linkedHashSet.add(null);
        linkedHashSet.add(null);
        linkedHashSet.add(" ");
        linkedHashSet.add("1");
        System.out.println(JSON.toJSON(linkedHashSet));
    }

    @Test
    public void testTreeSet() {
        TreeSet treeSet = new TreeSet();
        treeSet.add(null);
    }
}
