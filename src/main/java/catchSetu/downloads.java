package catchSetu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


@EnableRetry
public class downloads {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(downloads.class);
    public static String PATH = "D:\\picCollect\\test";
    public static String OutputMode = "hight";
    public static int ThreadNum = 3;

    /**
     * url下载文件
     * @param urlStr
     * @param fileName
     * @param savePath
     * @return
     */
    @Retryable
    public static String downLoadFromUrl(String urlStr, String fileName, String savePath) {
        boolean Key = true;
        int flag = 0;
        while (Key){
            try {
                if (flag > 0 && flag <5){
                    logger.debug("重新尝试下载第"+flag+"次。");
                }else if (flag == 0){

                } else {
                    logger.error("重试次数过多，放弃下载:"+urlStr);
                    logger.error("检查网络设置是否正常!");
                }
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 设置超时间为3秒
                conn.setConnectTimeout(10 * 1000);
                conn.setReadTimeout(10 * 1000);
                // 防止屏蔽程序抓取而返回403错误
                conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

                // 得到输入流
                InputStream inputStream = conn.getInputStream();
                // 获取自己数组
                byte[] getData = readInputStream(inputStream);

                // 文件保存位置
                File saveDir = new File(savePath);
                if (!saveDir.exists()) {
                    saveDir.mkdir();
                }
                File file = new File(saveDir + File.separator + fileName);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(getData);
                if (fos != null) {
                    fos.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                // System.out.println("info:"+url+" download success");
                Key = false;
                return saveDir + File.separator + fileName;
            } catch (Exception e) {
                logger.debug("url:"+urlStr+"下载失败，重试准备启动！");
                flag++;
                e.printStackTrace();
            }
        }
        return "";

    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException, IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    //下载线程3个
    @EnableRetry
    @Retryable
    class download extends Thread{
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
                downLoadFromUrl(entry.getValue().toString(),entry.getKey()+".jpg",PATH);
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
        }
    }

    public void DownloadFactory(JSONArray pics) throws InterruptedException {
        logger.info("=========启动下载=========");
        int length = pics.size();
        logger.info("任务总计:"+length);

        String qualify = "originalurl";
        if (OutputMode.equals("hight")){
            qualify = "originalurl";
        }else {
            qualify = "largeurl";
        }

        JSONArray tempJson = new JSONArray();
        logger.info("获取启动下载线程数量:"+ThreadNum);
        System.out.println("分组数量："+length / ThreadNum);
        for (int i=0; i<ThreadNum; i++){
            JSONObject temp = new JSONObject();
            for (int j=(length / ThreadNum) * i; j< (((length / ThreadNum)*(i+2)>length) ? length: (length / ThreadNum)*(i+1)); j++){
                JSONObject tt = pics.getJSONObject(j);
                temp.put(tt.getString("pid"), tt.getString(qualify));
            }
            tempJson.add(temp);
        }

         //线程池
         ExecutorService service = Executors.newFixedThreadPool(3);

        for (int i=0; i<ThreadNum; i++){
            service.execute(new download(tempJson.getJSONObject(i)));
        }
//         service.execute(new download(t1));
//         service.execute(new download(t2));
//         service.execute(new download(t3));
         service.shutdown();

     }

}
