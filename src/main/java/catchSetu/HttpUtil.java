package catchSetu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;


public class HttpUtil {
	public static void main(String[] args) {
	}

    public static void download(String urlString, String filename,String savePath){  
        // 构造URL  
        URL url;
		try {
			url = new URL(urlString);
	        URLConnection con = url.openConnection();  
	        con.setConnectTimeout(5*1000);  
	        InputStream is = con.getInputStream();  
	        byte[] bs = new byte[1024];  
	        int len;  
	       File sf=new File(savePath);  
	       if(!sf.exists()){  
	           sf.mkdirs();  
	       }  
	       OutputStream os = new FileOutputStream(sf.getPath()+"/"+filename);  
	        while ((len = is.read(bs)) != -1) {  
	          os.write(bs, 0, len);  
	        }  
	        os.close();  
	        is.close();  
		} catch (Exception e) {
			e.printStackTrace();
		}  
       
    }   
    
    
    public static String Get(String str,String code) {
    	if(code==null | "".equals(code)){
    		code="utf-8";
    	}
		StringBuilder sb = new StringBuilder();
		URL url;
		try {
			 url = new URL(str);
			 URLConnection urlConnection = url.openConnection();
			 urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
			 urlConnection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			 urlConnection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			 urlConnection.addRequestProperty("cookie","first_visit_datetime_pc=2019-12-09+12:28:42; p_ab_id=4; p_ab_id_2=5; p_ab_d_id=2129197772; yuid_b=EpRTIBI; _ga=GA1.2.2023649560.1575862140; a_type=0; b_type=1; login_ever=yes; d_type=4; adr_id=D7h8RUhbyYcJQRzSWkg5YvMtEum4faxBzJNkp9YQfDKQwIn3; ki_r=; __gads=ID=95f49a681bff2c3f:T=1578845138:S=ALNI_MYthFY-Yt05Q2MJxZm0WUV92ECNxg; __utmv=235335808.|2=login ever=yes=1^3=plan=normal=1^5=gender=male=1^6=user_id=19853173=1^9=p_ab_id=4=1^10=p_ab_id_2=5=1^11=lang=zh=1; PHPSESSID=19853173_CrC11fMYxyo1okFvi56yDGY7FAo5Luqz; privacy_policy_agreement=2; c_type=22; __cfduid=d9f63dae686fd191182b4d4a0c0c87cd41611886245; __utmz=235335808.1612240645.67.8.utmcsr=fanbox.cc|utmccn=(referral)|utmcmd=referral|utmcct=/; ki_s=204981:0.0.0.0.0;212334:0.0.0.0.0;213428:0.0.0.0.0; __utmc=235335808; tag_view_ranking=0xsDLqCEW6~EZQqoW9r8g~-98s6o2-Rp~2R7RYffVfj~Txs9grkeRc~Lt-oEicbBr~RTJMXD26Ak~_hSAdpN9rx~O2wfZxfonb~cuF6-_UeSz~qtVr8SCFs5~HY55MqmzzQ~PHQDP-ccQD~LmbPyhfNiW~MWWoQ-PzdL~pGv7p05oAU~kfohPZSK7g~MJvgZqqEAe~xVHdz2j0kF~Uhg1g_SJrF~K6-pVOVl7b~ko30YJxw7F~MJQbpCfxYq~kP7msdIeEU~OT4SuGenFI~jk9IzfjZ6n~zyKU3Q5L4C~q3eUobDMJW~aKhT3n4RHZ~9oafiNFKrr~qIDsnltE2o~jH0uD88V6F~1mSdGm7ylO~PiTUqp8BcV~dNVwq9RP5a~azESOjmQSV~KN7uxuR89w~QaiOjmwQnI~_bee-JX46i~aLBjcKpvWL~fUS-Ay2M9Z~jhuUT0OJva~2TgTsLTv2r~eVxus64GZU~QYP1NVhSHo~8Le-BdaoRB~zIv0cf5VVk~cWYS7Go-82~qiO14cZMBI~oCR2Pbz1ly~liM64qjhwQ~zUV1dBrslN~uXRQFgCSKj~5MHKhxjr7r~h9r9YX0n2U~3mLXnunyNA~ahHegnNVxX~zXUuIJGfCn~ZbQH8rzj7J~HxXLXAkkh8~fp4fS89gZH~Pt9XriSgeT~E8vkum0JtI~BU9SQkS-zU~Hvc3ekMyyh~_EOd7bsGyl~5Z-Ks7Elpa~q303ip6Ui5~4TDL3X7bV9~wmxKAirQ_H~9esyjt-dEN~zI1jzXA3QO~D9BseuUB5Z~KvAGITxIxH~UBwhLy7Ngq~q_J28dYJ9d~AKT2U2P4W6~plqXT5B4--~SJK3YcGD-h~JxeEcfLEAZ~-CmM2r4Xs8~aTQjBvZytq~Ce-EdaHA-3~LLyDB5xskQ~uusOs0ipBx~kGYw4gQ11Z~0GyqV8JLK3~_pwIgrV8TB~N1ZFlV3Ql1~Ku2rtEHSDm~7AKcijMMXr~faHcYIP1U0~xha5FQn_XC~5jQydRTLzH~BtXd1-LPRH~NGpDowiVmM~R3lr4__Kr8~Ie2c51_4Sp~v0NUUDccsO~y8GNntYHsi; __utma=235335808.2023649560.1575862140.1614151495.1614155086.72; ki_t=1578674228879;1614151500933;1614156914299;25;58; __utmt=1; __utmb=235335808.5.10.1614155086; __cf_bm=9473cd886ea787ed30e600cd42b1679bb0cb947e-1614158027-1800-AVXoqQcBsIP63bCs3PAEQumVwQiTxglxZCH3k4n980pZ6vX+MlNDezSW1LEIbhQgDlntc3JyoT+JarOavjp7CMzFNgoNwP+izs/Ga/UNjetsjcu/I8QXIw1eyDcpcCKSglqDETsWKCtFghP2ogtwCHmba+/jof8CC8BUq7ptm2Aj9SOPSArc49gxe2dwjCbFMw==");
			 BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),code)); // 获取输入流
			 String line = null;
			 while ((line = br.readLine()) != null) {
			   sb.append(line + "\n");
			 }
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("访问地址失败！ URL="+str);
		}
		return sb.toString();
	}
    
    public static String Post(String url, String param,String code) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();;
        if(code == null){
        	code = "UTF-8";
        }
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            //conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),code));
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(),code));
            String line;
            while ((line = in.readLine()) != null) {
            	result.append(line);
            }
            
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！ URL="+url+"  param="+param);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result.toString();
    }    
    
    
    

}
