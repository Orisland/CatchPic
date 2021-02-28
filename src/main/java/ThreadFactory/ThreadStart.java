package ThreadFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.retry.annotation.EnableRetry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static catchSetu.Factory.DATA;
import static catchSetu.Factory.LOCK;
import static catchSetu.config.*;
import static tool.DateProcess.rDate;


@EnableRetry
public class ThreadStart {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ThreadStart.class);

    /**
     * 图片下载进程统一启动
     * @param pics
     * @throws InterruptedException
     */
    public static void DownloadFactory(JSONArray pics) throws InterruptedException {
        logger.info("=========启动下载=========");
        int length = pics.size();
        logger.info("任务总计:"+length);
        String qualify = "originalurl";
        if (OutputMode.equals("hight")){
            qualify = "originalurl";
        }else {
            qualify = "largeurl";
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("length",length);
        jsonObject.put("qualify",qualify);
        jsonObject.put("jsonArray",pics);

        //数据分片
        JSONObject object = (JSONObject) ThreadCut.ThreadCut(jsonObject);
        JSONArray tempJson = object.getJSONArray("list");
         //线程池
         ExecutorService service = Executors.newFixedThreadPool(object.getInteger("ThreadNumP"));

        LOCK = new CountDownLatch(object.getInteger("ThreadNumP"));   //上锁
        for (int i=0; i<object.getInteger("ThreadNumP"); i++){
            service.execute(new ThreadWork.download(tempJson.getJSONObject(i)));
        }
         service.shutdown();
     }

    public static void FindPicFactory() throws InterruptedException {
        String datetime[] = {StartTime, EndTime};
        String date = rDate(datetime);
        logger.info(date);
        int page = MaxPage;  //一共找多少页的图
        int model = FilterMode.equals("CN") ? 0 : 1;  //跳过模式,0:cn过滤，1:jp过滤，2:全部关闭
        int setuNum = Setu;    //涩图指数下限,涩图建议3起步
        List<String> cnlv = new ArrayList<String>();
        List<String> jplv = new ArrayList<String>();
//        String jpstr = con.getString("JPFilter");
        String[] list = CNFilter.split(",");
        for (int i=0; i<list.length; i++){
            cnlv.add(list[i]);
        }

        JSONObject tempdata = new JSONObject();
        tempdata.put("model",model);
        tempdata.put("cnlv",cnlv);
        tempdata.put("jplv",jplv);

        //数据分片
        JSONObject temp = (JSONObject)ThreadCut.ThreadCut(date);
        tempdata.put("date", temp.getJSONArray("list"));
        ExecutorService service = Executors.newFixedThreadPool(temp.getInteger("ThreadNumP"));
        LOCK = new CountDownLatch(temp.getInteger("ThreadNumP"));   //上锁
        DATA = new ThreadData();
        System.out.println("启动API信息查询模块...");
        for (int i=0; i<temp.getInteger("ThreadNumP"); i++){
            service.execute(new ThreadWork.FindPic(tempdata));
        }
        service.shutdown();
    }
}
