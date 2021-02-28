package catchSetu;

import java.io.*;
import static catchSetu.config.OutputDir;

public class outputjs {
    public static void main(String[] args) throws Exception {

        //字节流
//        byteOutStream();

        //字符流 (输出流中含有中文时使用字符流)
        charOutStream("");

    }
    public static void charOutStream(String string) throws Exception{
        // 1：利用File类找到要操作的对象
        File file = new File(OutputDir+"\\test.js");
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }

        //2：准备输出流
        Writer out = new FileWriter(file);
        out.write(string);
        out.close();

    }

    public static void byteOutStream() throws Exception {

        //1:使用File类创建一个要操作的文件路径
        File file = new File("D:" + File.separator + "demo" + File.separator + "test.txt");
        if(!file.getParentFile().exists()){ //如果文件的目录不存在
            file.getParentFile().mkdirs(); //创建目录

        }

        //2: 实例化OutputString 对象
        OutputStream output = new FileOutputStream(file);

        //3: 准备好实现内容的输出

        String msg = "HelloWorld";
        //将字符串变为字节数组
        byte data[] = msg.getBytes();
        output.write(data);
        //4: 资源操作的最后必须关闭
        output.close();

    }
}
