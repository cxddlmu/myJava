package httpClient;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void doTest() throws ClientProtocolException, URISyntaxException, IOException {
        HttpUtil util = HttpUtil.getInstance();
        InputStream in = util.doGet("https://kyfw.12306.cn/otn/leftTicket/init");
        String retVal = HttpUtil.readStream(in, HttpUtil.defaultEncoding);
        System.out.println(retVal);
    }

}
