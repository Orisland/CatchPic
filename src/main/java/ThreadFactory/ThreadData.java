package ThreadFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

import static catchSetu.Factory.LOCK;

public class ThreadData {
    JSONArray rs = new JSONArray();
    JSONObject temp;
    int i = 0;

    public JSONArray getRs() {
        for (Map.Entry<String, Object> entry : temp.entrySet()){
            System.out.println("放入线程数据:"+entry.getKey()+":"+entry.getValue());
            rs.addAll((JSONArray)entry.getValue());
        }
        LOCK.countDown();//解锁主线程
        return rs;
    }

    public ThreadData(){
        temp = new JSONObject();
    }

    /**
     * 祈祷线程安全
     * @param name
     * @param jsonArray
     */
    public void addtemp(String name ,JSONArray jsonArray){
        temp.put(name,jsonArray);
    }
}
