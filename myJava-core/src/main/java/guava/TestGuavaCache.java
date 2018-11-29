package guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;

/**
 * Created by cuixiaodong on 2017/3/10.
 */
public class TestGuavaCache {
    public static void main(String[] args) throws ExecutionException {
        LoadingCache<String, Obj> cache = CacheBuilder.newBuilder().maximumSize(2).weakKeys().softValues()
                .build(new CacheLoader<String, Obj>() {
            @Override
            public Obj load(String key) throws Exception {
                return new Obj(10,3);
            }
        });
        Obj obj_d = new Obj(1, 3);
        Obj obj_a = new Obj(2, 3);
        Obj obj_b = new Obj(3, 3);
        cache.put("d",obj_d);
        cache.put("a",obj_a);
        // get method is the same as get(key,new Callable)
        System.out.println(cache.get("d"));
        cache.put("b",obj_b);
        System.out.println(cache.get("a"));
    }
}
class Obj{
    private int i;
    private int j;
    public Obj(int i,int j) {
        this.i=i;
        this.j=j;
    }

    @Override
    public String toString() {
        return String.valueOf(i+j);
    }
}
