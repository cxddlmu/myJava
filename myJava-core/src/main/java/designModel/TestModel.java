package designModel;

import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by cuixiaodong on 2017/3/22.
 */
public class TestModel {

    @Test
    public void testObserver(){
        ObservableExtend observable = new ObservableExtend();
        observable.addObserver(new ObserverImpl());
        observable.addObserver(new ObserverImpl());
        observable.addObserver(new ObserverImpl());
        observable.setChanged();
        observable.notifyObservers("good");
        observable.notifyObservers();
        System.out.println(observable.countObservers());
    }

    @Test
    public void testTemplate() {
        new TemplateImpl().exec();
    }

    @Test
    public void testChain() {
        concreateProcess concreateProcess = new concreateProcess();
        concreateProcess1 concreateProcess1 = new concreateProcess1();
        concreateProcess.setSuccessor(concreateProcess1);
        System.out.println(concreateProcess.handle("3333"));
    }

    @Test
    public void testFacotry() {

    }

    @Test
    public void testStratogy() {

    }
}


@Setter
@Getter
abstract class Process{
    private Process successor;
    abstract Object handleProcess(Object obj);
    Object handle(Object obj){
        Object obj1=handleProcess(obj);
        if (successor != null) {
            return successor.handle(obj1);
        }
        return obj1;
    }
}
 class concreateProcess extends Process{


     @Override
     Object handleProcess(Object obj) {
         return obj+"111";
     }
 }
class concreateProcess1 extends Process{


    @Override
    Object handleProcess(Object obj) {
        return obj+"22222";
    }
}



class ObservableExtend extends Observable{
    @Override
    protected synchronized void setChanged() {
        super.setChanged();
    }
}

class ObserverImpl implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        System.out.println(arg);
    }
}
