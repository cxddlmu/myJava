package regex;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuixiaodong on 2017/4/2.
 */
public class TestAviator {
    public static void main(String[] args) {
        Long result = (Long) AviatorEvaluator.execute("(1+2)*(3/1)");
        System.out.println(result);
        String expression = "a==100 && (b>40 || c<0) && d=='中国'";
        // 编译表达式
        Expression compiledExp = AviatorEvaluator.compile(expression,true);
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("a", 100);
        env.put("b", 45);
        env.put("c", -199.100);
        env.put("d", "中国");
        // 执行表达式
        Boolean result1 = (Boolean) compiledExp.execute(env);
        System.out.println(result1);  // false
    }
}
