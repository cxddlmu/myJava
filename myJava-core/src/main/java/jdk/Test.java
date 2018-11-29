package jdk;

import sun.java2d.xr.MutableInteger;

/**
 * Created by cuixiaodong on 2017/4/8.
 */
public class Test {
    public static void main(String[] args) {


        MutableInteger mutableInteger = new MutableInteger(1);
        mutableInteger.setValue(4);
        mutableInteger.getValue();
    }

}
