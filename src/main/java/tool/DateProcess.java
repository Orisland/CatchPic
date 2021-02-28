package tool;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static catchSetu.config.BLANK;

public class DateProcess {
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
}
