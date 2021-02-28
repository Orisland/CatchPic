package catchSetu;

import ThreadFactory.ThreadData;
import ThreadFactory.ThreadStart;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import tool.Stander;

import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import static ThreadFactory.ThreadStart.DownloadFactory;
import static ThreadFactory.ThreadStart.FindPicFactory;
import static catchSetu.config.*;
import static tool.fileDir.Filexists;
import static catchSetu.outputjs.charOutStream;

public class Factory {
    static boolean OutputRes = true;
    public static CountDownLatch LOCK = null;
    public static ThreadData DATA = null;

    public static void shouye(){
        System.out.println("=======================================");
        System.out.println("            P站周榜下载");
        System.out.println("");
        System.out.println("");
        System.out.println("=======================================");
    }

    public static void Configuration(JSONObject js) throws InterruptedException {
        OutputDir = js.getString("OutputDir");
        BLANK = Integer.parseInt(js.getString("Blank"));
        OutputMode = js.getString("OutputMode");
        OutputRes = Boolean.parseBoolean(js.getString("OutputRes"));
        MODE = js.getString("Mode");
        ThreadNum = Integer.parseInt(js.getString("Thread"));
        URLThread = Integer.parseInt(js.getString("URLThread"));
        StartTime = js.getString("StartTime");
        EndTime = js.getString("EndTime");
        MaxPage = Integer.parseInt(js.getString("MaxPage"));
        API = js.getString("API");
        CNFilter = js.getString("CNFilter");
        JPFilter = js.getString("JPFilter");
        Setu = Integer.parseInt(js.getString("Setu"));
        //API暂时不可选。
        System.out.println("=====================================配置已应用完成!");
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        shouye();
        System.out.println("读取配置文件...");
        JSONObject js = Filexists();
        for (Map.Entry<String, Object> entry : js.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
        System.out.println("是否执行此配置？Y/N");
        if (scanner.nextLine().toUpperCase().equals("Y")){
            System.out.println("正在配置...");
            Configuration(js);
//            System.out.println(P_rank(js));
            FindPicFactory();
            System.out.println("正在等待资料获取进程结束...");
            LOCK.await();   //上锁
            JSONArray rs = DATA.getRs();
            System.out.println(Stander.repack(rs));
            if (OutputRes){
                charOutStream(Stander.repack(rs));
            }
            DownloadFactory(rs);
        }else {
            System.out.println("结束！");
        }



    }
}
