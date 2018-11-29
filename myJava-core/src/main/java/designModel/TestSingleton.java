package designModel;

/**
 * Created by cuixiaodong on 2017/3/27.
 */
public class TestSingleton {
    public static void main(String[] args) {
        LazyModelRefine.getLazyModelRefine();
        LazyModelEnum.singleton.getSingleton();
    }
}

class PositiveModel {
    private PositiveModel() {
    }

    private static PositiveModel positiveModel = new PositiveModel();

    public static PositiveModel getPositiveModel() {
        return positiveModel;
    }
}

class LazyModel {
    private LazyModel() {
    }
    private static LazyModel lazyModel;

    public static synchronized LazyModel getLazyModel() {
        if (lazyModel == null) {
            lazyModel = new LazyModel();
            return lazyModel;
        }
        return lazyModel;
    }
}

class LazyModelRefine {
    private LazyModelRefine() {
    }
    private static class init {
        private static LazyModelRefine lazyModelRefine= new LazyModelRefine();
    }

    public static LazyModelRefine getLazyModelRefine() {
        return init.lazyModelRefine;
    }
}

enum LazyModelEnum {
    singleton;
    private TestSingleton testSingleton;
    LazyModelEnum() {
        testSingleton = new TestSingleton();
    }
    public TestSingleton getSingleton() {
        return testSingleton;
    }
}
