package rxJava;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * Created by cuixiaodong on 2018/3/13.
 */
public class Test {
    public static void main(String[] args) {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

                emitter.onNext(1);
            }
        });

        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {

            }
        };

//    observable.subscribeOn(Schedulers.newThread())
//
//
//            .observeOn(AndroidSchedulers.mainThread())
//
//
//            .subscribe(consumer);
//
//        }
//    }
    }
}
