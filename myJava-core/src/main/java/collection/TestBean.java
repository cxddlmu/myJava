package collection;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by cuixiaodong on 2017/3/4.
 */
@Setter
@Getter
public class TestBean implements Serializable {
    public TestBean() {
    }

    public TestBean(Integer id, String name) {
        this.id=id;
        this.name=name;
    }

    private Integer id;
    private String name;
}
