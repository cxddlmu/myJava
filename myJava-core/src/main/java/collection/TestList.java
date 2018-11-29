package collection;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by cuixiaodong on 2017/3/5.
 */
public class TestList {
    public static void main(String[] args) {
        testLinkedList();
        testArrayList();
        testCopyAndWritList();
        testListMethod();
    }

    private static void testListMethod() {
        String [] s = {"1", "2"};
        List<String> stringList = Arrays.asList(s);
        //stringList.add(null);//UnsupportedOperationException
        List<String> stringList1 = new ArrayList<String>(stringList).subList(0, 1);
        stringList1.add("5");
        List<Integer> intList = Lists.newArrayList(1,2,3,4);
        List<Integer> subList = intList.subList(1, 2);

    }

    private static void testLinkedList() {
        Integer [] a = {1,2,3,4,5};
        ArrayList<Integer> integers = new ArrayList<Integer>(0);
        System.out.println(integers.size());
        LinkedList linkedList = Lists.newLinkedList(Arrays.asList(a));
        linkedList.offer(77);
        linkedList.offerFirst(78);
        linkedList.offerLast(0);
        linkedList.add(99);
        linkedList.addFirst(77);
        System.out.println(JSON.toJSON(linkedList));
        System.out.println(linkedList.pop());
        linkedList.push(100);
        System.out.println(linkedList.peek());
        System.out.println(JSON.toJSON(linkedList));
        ArrayList<String> list = new ArrayList<String>();
        testCopyAndWritList();
    }

    private static void testArrayList() {
        ArrayList<Integer> arrayList = Lists.newArrayList();
        arrayList.add(1);
        arrayList.add(4);
        arrayList.add(4);
        arrayList.add(4);
        arrayList.add(4);
        arrayList.add(4);
        arrayList.add(null);
        arrayList.add(null);
        arrayList.add(4);
        System.out.println("JSON.toJSON(arrayList) = " + JSON.toJSON(arrayList));
        ListIterator listIterator = arrayList.listIterator();
        while (listIterator.hasNext()) {
//            arrayList.remove(listIterator.next());
            listIterator.next();
            listIterator.remove();
        }
       /* arrayList.add(1);
        arrayList.add(4);
        arrayList.add(14);
        arrayList.add(24);
        for (Integer i : arrayList) {
            arrayList.remove(i);
        }*/
        System.out.println("JSON.toJSON(arrayList) = " + JSON.toJSON(arrayList));
    }

    private static void testCopyAndWritList(){
        CopyOnWriteArrayList<Integer> copyOnWriteArrayList = Lists.newCopyOnWriteArrayList();
        copyOnWriteArrayList.addIfAbsent(1);
        copyOnWriteArrayList.add(14);
        copyOnWriteArrayList.add(24);
        copyOnWriteArrayList.add(1);
        copyOnWriteArrayList.add(1);
        copyOnWriteArrayList.add(1);
        copyOnWriteArrayList.add(1);
        copyOnWriteArrayList.add(1);
        ListIterator<Integer> integerListIterator = copyOnWriteArrayList.listIterator();
        while (integerListIterator.hasNext()) {
            Integer next = integerListIterator.next();
            copyOnWriteArrayList.remove(next);
        }
        System.out.println(JSON.toJSON(copyOnWriteArrayList));

    }
}
