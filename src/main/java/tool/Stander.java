package tool;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;


//规范数据专用
public class Stander {
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
     * 还原List
     * @param string
     * @return
     */
    public static List<String> BackList(String string){
        List<String> strings = new ArrayList<String>();
        String[] list = string.split(",");
        for (int i=0; i<list.length; i++){
            strings.add(list[i]);
        }
        return strings;
    }
}
