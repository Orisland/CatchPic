package ThreadFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import netscape.javascript.JSObject;

import java.util.ArrayList;
import java.util.List;

import static catchSetu.config.ThreadNum;
import static catchSetu.config.URLThread;

//线程分割任务
public class ThreadCut {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ThreadCut.class);

    public static Object ThreadCut(Object object){
        if (object instanceof JSONObject){
            return JsInput((JSONObject) object);
        }else if (object instanceof String){
            return StrInput((String)object);
        }

        else {
            logger.error("输入格式错误!");
            return null;
        }
    }

    //download专用,有点写死了,如果需要后续操作需要重写这个东西
    public static JSONObject JsInput(JSONObject jsonObject){
        int length = jsonObject.getInteger("length");
        String qualify = jsonObject.getString("qualify");
        JSONArray jsonArray = jsonObject.getJSONArray("jsonArray");
        int ThreadNumP = ThreadNum;

        if (ThreadNum > length){
            logger.debug("线程分配过量，修正为等长.");
            ThreadNumP = length;
        }

        logger.info("获取启动线程数量:"+ThreadNumP);
        logger.info("分组数量："+length / ThreadNumP);
        JSONObject object = new JSONObject();
        JSONArray tempJson = new JSONArray();
        for (int i=0; i<ThreadNumP; i++){
            JSONObject temp = new JSONObject();
            for (int j=(length / ThreadNumP) * i; j< (((length / ThreadNumP)*(i+2)>=length) ? length: (length / ThreadNumP)*(i+1)); j++){
                JSONObject tt = jsonArray.getJSONObject(j);
                temp.put(tt.getString("pid"), tt.getString(qualify));
            }
            tempJson.add(temp);
        }
        object.put("list",tempJson);
        object.put("ThreadNumP",ThreadNumP);

        return object;
    }

    public static JSONObject StrInput(String string){
        int length  = string.split(",").length;
        String[] strings = string.split(",");
        int ThreadNumP = URLThread;

        if (URLThread > length){
            System.out.println("线程上限设置过量，重置为任务数量!");
            ThreadNumP = length;
        }

        logger.info("获取启动加载线程数量:"+ThreadNumP);
        logger.info("分组数量："+length / ThreadNumP);
        JSONArray list = new JSONArray();
        JSONObject object = new JSONObject();
        for (int i=0; i<ThreadNumP; i++){
            String temp = "";
            for (int j=(length / ThreadNumP) * i; j< (((length / ThreadNumP)*(i+2)>=length) ? length: (length / ThreadNumP)*(i+1)); j++){
                String tt = strings[j];
                temp += tt + ",";
            }
            list.add(temp);
        }

        object.put("list",list);
        object.put("ThreadNumP",ThreadNumP);
        return object;
    }
}
