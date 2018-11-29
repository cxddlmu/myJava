package basic;

import org.junit.Test;

/**
 * Created by cuixiaodong on 2017/3/7.
 */
public class TestString {
    public static void main(String[] args) {
        String s = "abcd";
        s.substring(0);
        Integer integer = 200;
        Integer integer1 = 200;

        Integer integer2 = 127;
        Integer integer3 = 127;
        System.out.println(integer.equals(integer1));
        System.out.println(integer3==integer2);
    }

    @Test
    public void testString1() {
        String s = "abcdefg";
        System.out.println("s.substring(1,2); = " + s.substring(1,2));
        System.out.println("s = " + s.subSequence(1, 2));
        System.out.println("s.indexOf(\"a\") = " + s.indexOf("efg"));
        String s1=s.intern();
        System.out.println(s1==s);//true
        s.toCharArray();
        s.startsWith("b");
        s.indexOf('a');
        char a = 'b';
        Character character = 'b';
        int b= 'b';
        System.out.println(a);
        System.out.println(b);
    }
}
