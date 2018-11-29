package jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuixiaodong on 2017/3/4.
 */
public class TestJvm {
    private static final int _1MB=1024*1024;

    public static void main(String[] args) {
        testAllocation();
    }
    public static void testAllocation(){
        /*
       java -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 TestJvm
         */
        byte[] allocation1,allocation2,allocation3;
        allocation1=new byte[3*_1MB];
        allocation2=new byte[3*_1MB];
        allocation3=new byte[1*_1MB];
        //allocation4=new byte[1*_1MB];//出现一次Minor GC
    }
}
class TestMemLeak{
    public static void main(String[] args) throws InterruptedException {
                List list = new ArrayList();
                Runtime run = Runtime.getRuntime();
                int i=1;
                while(true){
                    int[] arr = new int[1024 * 8];
                    list.add(arr);
                    if(i++ % 1000 == 0 ){
                        System.out.print("i="+i );
                        System.out.print("最大内存=" + run.maxMemory() / 1024 / 1024 + "M,");
                        System.out.print("已分配内存=" + run.totalMemory() /1024 / 1024 + "M,");
                        System.out.print("剩余空间内存=" + run.freeMemory() / 1024 / 1024 + "M");
                        System.out.println("最大可用内存=" + ( run.maxMemory() - run.totalMemory() + run.freeMemory() ) / 1024 / 1024 + "M");
                        Thread.sleep(6*1000L);
                    }
                }
            }
}
