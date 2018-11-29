package basic;

import collection.TestBean;
import com.alibaba.fastjson.JSON;

/**
 * Created by cuixiaodong on 2017/3/4.
 */
public class TestMethod extends ParentMed{
    public static void main(String[] args) {
        TestBean testBean = new TestBean();
        //new TestMethod().test1(testBean);
        System.out.println(JSON.toJSON(testBean));
        char a='1';
        ParentMed parentMed = new TestMethod();
        TestMethod med = (TestMethod) parentMed;
//        med.test1(testBean);
//        parentMed.test1(testBean);
        pt pt = new pt();
        ParentMed pt1 = (ParentMed) pt;
    }

    public void test1(TestBean testBean) {
//        super.test1(testBean);
        testBean.setId(1);
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void test2(int i) {
        System.out.println("childMed2");
    }
    static void test3() {
        System.out.println("ParentMed2");
    }
}
class ParentMed extends pt{
    public void test1(TestBean testBean) {
        System.out.println("parentMed");
        //new TestMethod().test2(1);


    }
    public void test2(int i) {
        System.out.println("ParentMed2");
    }
    public void test2() {
        System.out.println("ParentMed2");
    }
    static void test3() {
        System.out.println("ParentMed2");
    }
    private static void test3(int i) {
        System.out.println("ParentMed2");
    }

}
class pt{}
