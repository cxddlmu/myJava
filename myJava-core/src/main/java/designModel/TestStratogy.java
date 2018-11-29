package designModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuixiaodong on 2017/4/2.
 */
public class TestStratogy {
    public static void main(String[] args) {
        validStratory validStratory = new validStratory();
        validStratory.addStrategy(new NumberValid());
        validStratory.addStrategy(new StringValid());
        validStratory.exec("2");
    }
}
class validStratory{
    List<Strategy> validList = new ArrayList<>();
    void set(List<Strategy> validList) {
        this.validList=validList;
    }

    void addStrategy(Strategy strategy) {
        validList.add(strategy);
    }

    void exec(Object obj){

    }
}
interface Strategy{
    void valid();
}
class NumberValid implements Strategy{
    @Override
    public void valid() {
        System.out.println("number");
    }
}
class StringValid implements Strategy{
    @Override
    public void valid() {
        System.out.println("string");
    }
}