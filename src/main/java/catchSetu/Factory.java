package catchSetu;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import static catchSetu.downloads.ThreadNum;
import static catchSetu.fileDir.Filexists;
import static catchSetu.catchPic.P_rank;
import static catchSetu.downloads.PATH;
import static catchSetu.catchPic.BLANK;
import static catchSetu.catchPic.OutputMode;
import static catchSetu.catchPic.*;
import static catchSetu.outputjs.charOutStream;

public class Factory {
    static boolean OutputRes = true;

    public static void shouye(){
        System.out.println("=======================================");
        System.out.println("            P站周榜下载");
        System.out.println("");
        System.out.println("");
        System.out.println("=======================================");
    }

    public static void Configuration(JSONObject js) throws InterruptedException {
        PATH = js.getString("OutputDir");
        BLANK = Integer.parseInt(js.getString("Blank"));
        OutputMode = js.getString("OutputMode");
        OutputRes = Boolean.getBoolean(js.getString("OutputRes"));
        MODE = js.getString("Mode");
        ThreadNum = Integer.parseInt(js.getString("Thread"));
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
            System.out.println(P_rank(js));
            System.out.println(repack(tj));
            System.out.println("OUTPUTRES:"+OutputRes);
            if (OutputRes){
                charOutStream(repack(tj));
            }
            downloads downloads = new downloads();
            downloads.DownloadFactory(tj);
        }else {
            System.out.println("结束！");
        }



    }
}
