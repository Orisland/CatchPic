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
        System.out.println("            By Orisland");
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
        Outfile = js.getString("Outfile");
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
        System.out.println("\n");
        System.out.println("如需修改请退出bat，并在同级目录中寻找config.txt文件打开修改...");
        System.out.println("闪退或恢复默认设置请删除config.txt");
        System.out.println("是否执行此配置？Y/N");
        if (scanner.nextLine().toUpperCase().equals("Y")){
            System.out.println("正在配置...");
            Configuration(js);
//            System.out.println(P_rank(js));
            FindPicFactory();
            System.out.println("正在等待资料获取进程结束...");
            LOCK.await();   //上锁
            JSONArray rs = DATA.getRs();
            System.out.println("==================");
            System.out.println("信息查询模块执行结束！");
            System.out.println("==================");
            System.out.println("\n");
            System.out.println("==================");
            System.out.println("下载模块即将在3秒后启动!");
            System.out.println("任务长度:"+rs.size());
            System.out.println("==================");
            Thread.sleep(3000);
            System.out.println(Stander.repack(rs));
            if (OutputRes){
                charOutStream(Stander.repack(rs));
            }
            DownloadFactory(rs);
            LOCK.await();       //上锁
            System.out.println("任务长度:"+rs.size());
            System.out.println("已全部下载完毕~~");
            System.out.println("\n");
            System.out.println("感谢宁的使用哦！请注意身体健康！");
            System.out.println("感谢宁的使用哦！请注意身体健康！");
            Runtime run = Runtime.getRuntime();
            run.exec("cmd.exe /c " + "start "+OutputDir);
        }else {
            System.out.println("结束！");
        }



    }
}
