package catchSetu;

import com.alibaba.fastjson.JSONObject;

import java.io.*;

public class fileDir {

    /**
     * 若配置文件不存在，写入初始配置文件
     * @throws IOException
     */
    public static JSONObject Filexists() throws IOException {
        File file = new File("config.txt");
        if (!file.exists()){
            file.createNewFile();
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
                String string = "StartTime=2021-1-1\n" +
                        "EndTime=2021-2-1\n" +
                        "Blank=7\n" +
                        "Mode=week\n" +
                        "MaxPage=5\n" +
                        "OutputMode=hight\n" +
                        "OutputDir=D:\\picCollect\\test\n" +
                        "API=Pixiv\n" +
                        "FilterMode=CN\n" +
                        "CNFilter=正太,漫画,manga,作画过程,R-18,男孩子,乳环,父乳,メイキング,作画,动图,精灵宝可梦,宣传,四格漫画,马里奥,画法,sex education,性教育,4コマ,photoshop,讲座\n" +
                        "JPFilter=\n" +
                        "OutputListAll=false\n" +
                        "OutputFilter=false\n" +
                        "OutputRes=true\n" +
                        "Proxy=https://blue-dawn-a7a7.orisland.workers.dev/\n" +
                        "Setu=3\n" +
                        "Thread=3";
                byte[] buff = string.getBytes();
                fileOutputStream.write(buff);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                fileOutputStream.close();
            }
            return FileInput();
        }else {
            return FileInput();
        }
    }

    /**
     * 若配置文件存在，读取配置文件
     * @return
     * @throws IOException
     */
    public static JSONObject FileInput() throws IOException {
        JSONObject js = new JSONObject();
        FileReader fileReader = null;
        BufferedReader bf = null;
        try {
            fileReader = new FileReader(new File("config.txt"));
            bf = new BufferedReader(fileReader);
            String str = "";
            while ((str = bf.readLine()) != null){
                String[] strings = str.split("=");
                if (strings.length == 1){
                    js.put(strings[0],"");
                }else {
                    js.put(strings[0],strings[1]);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {

            bf.close();
        }
        return js;
    }

    public void Config(){

    }

    public static void main(String[] args) throws IOException {
        System.out.println(Filexists());
    }
}
