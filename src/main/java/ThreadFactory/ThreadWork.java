package ThreadFactory;


import tool.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import tool.Stander;

import java.util.List;
import java.util.Map;

import static catchSetu.Factory.DATA;
import static catchSetu.config.*;
import static tool.HttpUtil.downLoadFromUrl;
import static tool.Stander.reJson;
import static catchSetu.config.Proxy;
import static catchSetu.config.Setu;
import static catchSetu.config.CNFilter;
import static catchSetu.Factory.LOCK;

public class ThreadWork {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ThreadWork.class);

    /**
     * 下载图片进程
     */
    @EnableRetry
    @Retryable
    public static class download extends Thread{
        JSONObject pic;

        public download(JSONObject pic) {
            this.pic = pic;
        }

        @Override
        @Retryable
        public void run() {
            for (Map.Entry<String, Object> entry : pic.entrySet()) {
                long startTime = System.currentTimeMillis();
                System.out.println(entry.getValue().toString()+":"+entry.getKey());
                downLoadFromUrl(entry.getValue().toString(),entry.getKey()+".jpg",OutputDir);
                long endTime = System.currentTimeMillis();
                double time = (endTime - startTime) / 1000;
                logger.info(currentThread().getName()+":"+entry.getKey()+".jpg 下载完成！" + "用时：" + time + "s");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            logger.info(currentThread().getName()+":任务结束！=======================================================任务长度"+pic.size());
            LOCK.countDown();
        }
    }

    /**
     * 图片详细信息数据进程
     */
    static class FindPic extends Thread{
        JSONObject jsonObject;
        int jump1 = 0;   //涩图指数跳过
        int jump2 = 0;   //涩图标签跳过
        int addpic = 0;    //入库
        boolean jump = false;
        int model;
        String date;
        List<String> cnlv;
        List<String> jplv;

        static JSONObject rsobj = new JSONObject();
        static JSONArray tj = new JSONArray();
        static JSONArray lv = new JSONArray();


        public FindPic(JSONObject jsonObject) throws InterruptedException {
            this.jsonObject = jsonObject;
            init();
        }

        public void init() throws InterruptedException {
            model= jsonObject.getInteger("model");
            date = jsonObject.getString("date");
            cnlv = Stander.BackList(CNFilter);
            jplv = Stander.BackList(JPFilter);
        }

        @Override
        public void run() {
            JSONArray jsonArray = JSONArray.parseArray(date);
            JSONArray illlist = null;
            JSONObject jsrs = null;
            for (int l=0; l<jsonArray.size(); l++){
                for (String str : jsonArray.getString(l).split(",")){
                    for (int g=1; g<=MaxPage; g++){
                        boolean KEY = true;
                        int flag = 0;
                        while (KEY){
                            if (flag > 0 && flag <3){
                                logger.debug("重新尝试访问第"+flag+"次。");
                            }else if (flag == 0){

                            } else {
                                logger.error("重试次数过多，放弃下载:"+str);
                                logger.error("检查网络设置是否正常!");
                            }
                            try {
                                jsrs = JSONObject.parseObject(HttpUtil.Get("https://orislandapi.herokuapp.com/api/pixiv/rank?page="+g+"&date="+str+"&mode="+MODE,""));
                                logger.info(currentThread().getName()+"=============API已获取相关数据！page:"+g+",date:"+str+"=============");
                                illlist = jsrs.getJSONArray("illusts");
                                for (int t=0; t<illlist.size(); t++){
                                    JSONObject illitem = illlist.getJSONObject(t);
                                    illitem.put("Proxy",Proxy);
                                    if (Integer.parseInt(illitem.getString("sanity_level")) <= Setu){
                                        logger.info("其涩图指数为:"+illitem.getString("sanity_level")+",不符合涩图指数,跳过!");
                                        jump = false;
                                        jump1++;
                                        continue;
                                    }else {
                                        JSONArray illtags = JSONArray.parseArray(illlist.getJSONObject(t).getString("tags"));
                                        for (int i=0; i<illtags.size(); i++){
                                            String cntill = illtags.getJSONObject(i).getString("translated_name");
                                            String jptill = illtags.getJSONObject(i).getString("name");
                                            if (model == 0){
                                                if (cntill == null)
                                                    cntill = "null";
                                                for (int j=0; j<cnlv.size(); j++){
                                                    if (cntill.equals(cnlv.get(j))){
                                                        logger.info(illitem.getString("id")+":匹配发现过滤cntag:"+cntill+"，跳过！");
                                                        lv.add(illitem);
                                                        jump2++;
                                                        jump = true;
                                                        break;
                                                    }
                                                }
                                            }else if (model == 1){
                                                for (int j=0; j<jplv.size(); j++){
                                                    if (jptill.equals(jplv.get(j))){
                                                        logger.info("匹配发现过滤jptag:"+jptill+"，跳过！");
                                                        jump = true;
                                                        jump2++;
                                                        break;
                                                    }
                                                }
                                            }else {
                                                break;
                                            }
                                        }
                                    }

                                    if (!jump){
                                        illitem = reJson(illitem);
                                        rsobj.put(illitem.getString("pid"),illitem);
                                        logger.info("符合条件！入库！");
                                        tj.add(illitem);
                                        addpic++;
                                        jump = false;
                                    }
                                }
                                logger.info(currentThread().getName()+"=============API已完成相关数据获取！page:"+g+",date:"+str+"=============");
                                KEY = false;
                            }catch (Exception e){
                                e.printStackTrace();
                                logger.debug("即将重启，API访问失败结果:"+jsrs);
                                flag++;
                            }
                        }
                    }
                }
            }
            DATA.addtemp(currentThread().getName(),tj);
            logger.info(currentThread().getName()+":工作已完成！");
            LOCK.countDown();
        }
    }
}
