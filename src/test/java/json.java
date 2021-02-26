import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public class json {
    public void test1(){
        String jsonStr = "{\"size\":\"7.5\",\"width\":\"M (B)\"}";

        JSONObject jsonObj = JSON.parseObject(jsonStr);
        for (Map.Entry<String, Object> entry : jsonObj.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }

    public void test2(){

    }

    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        System.out.println(jsonObject.size());
    }
}
