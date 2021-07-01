package util;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用于发送Http请求的工具类
 *
 * @author zjx
 * @since 2021/6/10 9:48 下午
 */
public class HttpUtil {

    private final static String URI = "http://1.15.157.198:8080/";

//    private final static String URI = "http://127.0.0.1:8080/";

    private final static CloseableHttpClient client = HttpClients.createDefault();

    public static HttpEntity sendPost(String url, Map<String, String> parameter) {
        try {
            HttpPost httpPost = new HttpPost(URI + url);
            httpPost.setEntity(new StringEntity(JSON.toJSONString(parameter), ContentType.APPLICATION_JSON));

            CloseableHttpResponse response = client.execute(httpPost);

            return response.getEntity();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
