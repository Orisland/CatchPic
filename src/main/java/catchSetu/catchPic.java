package catchSetu;

import com.alibaba.fastjson.*;
import org.apache.log4j.*;

import catchSetu.downloads;

import javax.swing.plaf.IconUIResource;
import java.io.IOException;
import java.util.*;

import static catchSetu.downloads.downLoadFromUrl;
import static catchSetu.outputjs.charOutStream;
import static catchSetu.downloads.ThreadNum;

public class catchPic {

    private static final Logger logger = Logger.getLogger(catchPic.class);
    static JSONObject rsobj = new JSONObject();
    static JSONArray tj = new JSONArray();
    static JSONArray lv = new JSONArray();
    public static int BLANK=7;
    public static String MODE = "WEEK";
    public static String OutputMode = "hight";
    private static int ThreadNum = downloads.ThreadNum;

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
            downLoadFromUrl(rs,"test"+flag+".jpg","D:\\test");
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
        String cnstr = con.getString("CNFilter");
//        String jpstr = con.getString("JPFilter");
        String[] list = cnstr.split(",");
        for (int i=0; i<list.length; i++){
            cnlv.add(list[i]);
        }
        int jump1 = 0;   //涩图指数跳过
        int jump2 = 0;   //涩图标签跳过
        int addpic = 0;    //入库
        boolean jump = false;

        for (String str : date.split(",")){
            for (int g=1; g<=page; g++){
                JSONObject jsrs = JSONObject.parseObject(HttpUtil.Get("https://api.obfs.dev/api/pixiv/rank?page="+g+"&date="+str+"&mode="+MODE,""));
                logger.info("=============API已获取相关数据！page:"+g+",date:"+str+"=============");
                JSONArray illlist = jsrs.getJSONArray("illusts");
                for (int t=0; t<illlist.size(); t++){
                    JSONObject illitem = illlist.getJSONObject(t);
                    illitem.put("Proxy",con.getString("Proxy"));
                    if (Integer.parseInt(illitem.getString("sanity_level")) <= setuNum){
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
                logger.info("=============API已完成相关数据获取！page:"+g+",date:"+str+"=============");
            }
        }
        return "jump1:"+jump1+",jump2:"+jump2+",add:"+addpic;
    }

    /**
     * 转化为图库标准通用json
     * @param js
     * @return
     */
    public static JSONObject reJson(JSONObject js){
        JSONObject rs = new JSONObject();
        rs.put("pid",noNULL(Long.parseLong(js.getString("id"))));
        rs.put("p",noNULL(Long.parseLong(js.getString("page_count"))));
        if (js.getJSONObject("user") == null){
            rs.put("uid","null");
        }else {
            rs.put("uid",noNULL(Long.parseLong(js.getJSONObject("user").getString("id"))));
        }
        rs.put("title",noNULL(js.getString("title")));
        if (js.getJSONObject("user") == null){
            rs.put("author","null");
        }else {
            rs.put("author",noNULL(js.getJSONObject("user").getString("name")));
        }
        rs.put("width",noNULL(Long.parseLong(js.getString("width"))));
        rs.put("height",noNULL(Long.parseLong(js.getString("height"))));
        JSONArray jsonArray = js.getJSONArray("tags");


        JSONArray lists = new JSONArray();
        for (int i=0; i<jsonArray.size(); i++){
            if (jsonArray.getJSONObject(i).getString("name").equals(jsonArray.getJSONObject(i).getString("translated_name"))){
                lists.add(jsonArray.getJSONObject(i).getString("name"));
            }else if (jsonArray.getJSONObject(i).getString("translated_name") == null){
                lists.add(jsonArray.getJSONObject(i).getString("name"));
            }else {
                lists.add(jsonArray.getJSONObject(i).getString("name"));
                lists.add(jsonArray.getJSONObject(i).getString("translated_name"));
            }
        }
        rs.put("tags",lists);

        String rurl = js.getJSONObject("image_urls").getString("large");
        String url = "";
        if (js.getJSONObject("meta_single_page").size() == 0){
            Object object = js.get("meta_pages");
            if (object instanceof JSONObject){
                url = js.getJSONObject("meta_pages").getJSONObject("image_urls").getString("original");
            }else if(object instanceof JSONArray){
                url = js.getJSONArray("meta_pages").getJSONObject(0).getJSONObject("image_urls").getString("original");
            }

        }else {
            url = js.getJSONObject("meta_single_page").getString("original_image_url");
        }
        rs.put("largeurl",js.getString("Proxy") + rurl.substring(20));
        rs.put("originalurl",js.getString("Proxy") + url.substring(20));
        rs.put("url","https://i.pixiv.cat/" + url.substring(20));
        rs.put("path","setu_0096/"+rs.getString("pid")+".jpg");
//        rs.put("url","https://pixiv.cat/"+rs.getString("pid")+".jpg");
        rs.put("涩度",noNULL(Long.parseLong(js.getString("sanity_level"))));
        return rs;
    }

    /**
     * 输出涩图目录
     * @param array
     * @return
     */
    public static String repack(JSONArray array){
        String head = "setu_0096(";
        String tail = ")";
        String pack = head + array.toJSONString()+tail;

        return pack;
    }

    /**
     * 去除所有null
     * @param str
     * @return
     */
    public static String noNULL(String str){
        return str == null ? "null" : str;
    }

    public static Long noNULL(Long num){
        return num == null ? 0 : num;
    }

    /**
     * 规范化日期，周榜7天一统计
     * @param date
     * @return
     */
    public static String rDate(String[] date){
        String timelist = "";
        String[] start = date[0].split("-");
        String[] end = date[1].split("-");
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Integer.parseInt(start[0]),Integer.parseInt(start[1])-1,Integer.parseInt(start[2]));
        long startms = calendar.getTimeInMillis();
        calendar.set(Integer.parseInt(end[0]),Integer.parseInt(end[1])-1,Integer.parseInt(end[2]));
        long endms = calendar.getTimeInMillis();

        long sevenTime = BLANK*24*60*60*1000;
        //当前时间 毫秒数- 7天的毫秒数= 7天之后那天的毫秒数
        long times;

        while (true){
            if ((startms + sevenTime)>endms){
                break;
            }else {
                startms = startms + sevenTime;
                Date dat=new Date(startms);
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime(dat);
                java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
                timelist += format.format(gc.getTime()) + ",";
            }
        }
        return timelist;
    }

    public static void main(String[] args) throws Exception {
//        logger.info(P_rank(new String[]{"2020-7-1","2020-8-1"}));
        logger.debug(rsobj);
        System.out.println(repack(tj));
        charOutStream(repack(tj));
        System.out.println(tj);
        downloads downloads = new downloads();
        downloads.DownloadFactory(tj);
}
}
