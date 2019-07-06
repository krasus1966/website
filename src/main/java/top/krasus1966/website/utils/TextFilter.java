package top.krasus1966.website.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Krasus1966
 * @date 2020/4/9 13:05
 **/
public class TextFilter {

    public static Body textFilter(String src) {
        String host = "https://textfilter.xiaohuaerai.com";
        String path = "/textfilter";
        String method = "POST";
        String appcode = "03616602b4ee4a5394e690eea8c9d4b7";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("src", src);
        bodys.put("type", "easy");
        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            JSONObject jsonObject = (JSONObject) JSON.parse(EntityUtils.toString(response.getEntity()));
            Body body = new Body();
            body.setMsg((String) jsonObject.get("msg"));
            body.setRes((Integer) jsonObject.get("res"));
            body.setStatus((Integer) jsonObject.get("status"));
            return body;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
