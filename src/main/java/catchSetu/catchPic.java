package catchSetu;

import ThreadFactory.ThreadStart;
import ThreadFactory.ThreadWork;
import com.alibaba.fastjson.*;
import org.apache.log4j.*;

import tool.HttpUtil;

import java.util.*;

import static tool.DateProcess.rDate;
import static tool.Stander.*;
import static catchSetu.outputjs.charOutStream;
import static catchSetu.config.ThreadNum;
import static catchSetu.config.MODE;
import static catchSetu.config.CNFilter;

public class catchPic {

    private static final Logger logger = Logger.getLogger(catchPic.class);
    static JSONObject rsobj = new JSONObject();
    static JSONArray tj = new JSONArray();
    static JSONArray lv = new JSONArray();

    /**
     * loliconAPI取图
     * @return
     */
    public static String lolicoAPI(){
        int flag = 0;
        JSONObject jsrs = JSONObject.parseObject(HttpUtil.Get("https://api.lolicon.app/setu/?apikey=232368045f2bc262c4e5e4&size1200=true&num=10",""));
        if (jsrs.getString("code").equals("0")){
            logger.info("API获取json完成");
        }else {
            logger.error("API获取json出错");
            return null;
        }
        for(int i=0; i<JSONArray.parseArray(jsrs.getString("data")).size();i++){
            long startTime = System.currentTimeMillis();
            String rs = JSONArray.parseArray(jsrs.getString("data")).getJSONObject(i).getString("url");
            HttpUtil.downLoadFromUrl(rs,"test"+flag+".jpg","D:\\test");
            long endTime = System.currentTimeMillis();
            double time = (endTime - startTime)/1000d;
            logger.info("图"+i+"保存完成!  耗时:"+String.format("%.2f",time)+"秒");
            flag++;
        }

        return "";
    }


    /**
     * https://www.pixiv.net/ranking.php?mode=male&format=json
     * P站取rank图,由于原网站被保护加密，所以选择其他的api
     * @return
     */
    public static String P_rank(JSONObject con) throws InterruptedException {
        String datetime[] = {con.getString("StartTime"), con.getString("EndTime")};
        String date = rDate(datetime);
        logger.info(date);
        int page = Integer.parseInt(con.getString("MaxPage"));  //一共找多少页的图
        int model = con.getString("FilterMode").equals("CN") ? 0 : 1;  //跳过模式,0:cn过滤，1:jp过滤，2:全部关闭
        int setuNum = Integer.parseInt(con.getString("Setu"));    //涩图指数下限,涩图建议3起步
        List<String> cnlv = new ArrayList<String>();
        List<String> jplv = new ArrayList<String>();
//        String jpstr = con.getString("JPFilter");
        String[] list = CNFilter.split(",");
        for (int i=0; i<list.length; i++){
            cnlv.add(list[i]);
        }

        JSONObject tempdata = new JSONObject();
        tempdata.put("date", date);
        tempdata.put("model",model);
        tempdata.put("cnlv",cnlv);
        tempdata.put("jplv",jplv);

//        new ThreadStart().FindPicFactory(tempdata);

        int jump1 = 0;   //涩图指数跳过
        int jump2 = 0;   //涩图标签跳过
        int addpic = 0;    //入库
        boolean jump = false;

//        int length  = date.split(",").length;
//        List<String> datelist = new ArrayList<String>();
//        logger.info("获取启动加载线程数量:"+ThreadNum);
//        System.out.println("分组数量："+length / ThreadNum);
//        for (int i=0; i<ThreadNum; i++){
//            String temp = "";
//            for (int j=(length / ThreadNum) * i; j< (((length / ThreadNum)*(i+2)>length) ? length: (length / ThreadNum)*(i+1)); j++){
//                String tt = date.split(",")[j];
//                temp += tt + ",";
//            }
//            datelist.add(temp);
//        }
//
//
//        for (String str : date.split(",")){
//            for (int g=1; g<=page; g++){
//                JSONObject jsrs = JSONObject.parseObject(HttpUtil.Get("https://api.obfs.dev/api/pixiv/rank?page="+g+"&date="+str+"&mode="+MODE,""));
//                logger.info("=============API已获取相关数据！page:"+g+",date:"+str+"=============");
//                JSONArray illlist = jsrs.getJSONArray("illusts");
//                for (int t=0; t<illlist.size(); t++){
//                    JSONObject illitem = illlist.getJSONObject(t);
//                    illitem.put("Proxy",con.getString("Proxy"));
//                    if (Integer.parseInt(illitem.getString("sanity_level")) <= setuNum){
//                        logger.info("其涩图指数为:"+illitem.getString("sanity_level")+",不符合涩图指数,跳过!");
//                        jump = false;
//                        jump1++;
//                        continue;
//                    }else {
//                        JSONArray illtags = JSONArray.parseArray(illlist.getJSONObject(t).getString("tags"));
//                        for (int i=0; i<illtags.size(); i++){
//                            String cntill = illtags.getJSONObject(i).getString("translated_name");
//                            String jptill = illtags.getJSONObject(i).getString("name");
//                            if (model == 0){
//                                if (cntill == null)
//                                    cntill = "null";
//                                for (int j=0; j<cnlv.size(); j++){
//                                    if (cntill.equals(cnlv.get(j))){
//                                        logger.info(illitem.getString("id")+":匹配发现过滤cntag:"+cntill+"，跳过！");
//                                        lv.add(illitem);
//                                        jump2++;
//                                        jump = true;
//                                        break;
//                                    }
//                                }
//                            }else if (model == 1){
//                                for (int j=0; j<jplv.size(); j++){
//                                    if (jptill.equals(jplv.get(j))){
//                                        logger.info("匹配发现过滤jptag:"+jptill+"，跳过！");
//                                        jump = true;
//                                        jump2++;
//                                        break;
//                                    }
//                                }
//                            }else {
//                                break;
//                            }
//                        }
//                    }
//
//                    if (!jump){
//                        illitem = reJson(illitem);
//                        rsobj.put(illitem.getString("pid"),illitem);
//                        logger.info("符合条件！入库！");
//                        tj.add(illitem);
//                        addpic++;
//                        jump = false;
//                    }
//                }
//                logger.info("=============API已完成相关数据获取！page:"+g+",date:"+str+"=============");
//            }
//        }
        return "jump1:"+jump1+",jump2:"+jump2+",add:"+addpic;
    }

    public static void main(String[] args) throws Exception {
//        logger.info(P_rank(new String[]{"2020-7-1","2020-8-1"}));
        logger.debug(rsobj);
        System.out.println(repack(tj));
        charOutStream(repack(tj));
        System.out.println(tj);
        ThreadStart ThreadStart = new ThreadStart();
        ThreadStart.DownloadFactory(tj);
}
}
