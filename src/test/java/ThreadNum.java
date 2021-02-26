import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Random;

public class ThreadNum {
    public static void main(String[] args) {
        int length = 200;
        JSONArray tempJson = new JSONArray();
        int ThreadNum = 5;
        System.out.println("获取启动下载线程数量:" + ThreadNum);
        for (int i=0; i<ThreadNum; i++){
            JSONObject temp = new JSONObject();
            for (int j=(length / ThreadNum) * i; j< (((length / ThreadNum)*(i+1)>length) ? length: (length / ThreadNum)*(i+1)); j++){
                temp.put(String.valueOf(j),new Random().nextInt());
            }
            tempJson.add(temp);
        }

        System.out.println(tempJson);
    }
}
