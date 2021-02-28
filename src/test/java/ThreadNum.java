import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static catchSetu.config.ThreadNum;

public class ThreadNum {
    public static void download(){
        int length = 200;
        JSONArray tempJson = new JSONArray();

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

    public static void main(String[] args) {
        String date = "2020-01-08,2020-01-15,2020-01-22,2020-01-29,2020-02-05,2020-02-12,2020-02-19,2020-02-26,";
        int length  = date.split(",").length;
        System.out.println(length);
        if (ThreadNum > length){
            System.out.println("线程上限设置过量，重置为任务数量!");
            ThreadNum = length;
        }
        List<String> datelist = new ArrayList<String>();
        System.out.println("获取启动加载线程数量:"+ThreadNum);
        System.out.println("分组数量："+length / ThreadNum);
        for (int i=0; i<ThreadNum; i++){
            String temp = "";
            for (int j=(length / ThreadNum) * i; j< (((length / ThreadNum)*(i+2)>length) ? length: (length / ThreadNum)*(i+1)); j++){
                String tt = date.split(",")[j];
                temp += tt + ",";
            }
            System.out.println(temp);
            datelist.add(temp);
        }

        System.out.println(datelist.toString());
    }
}
