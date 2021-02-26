import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class date {

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

        long sevenTime = 7*24*60*60*1000;
        //当前时间 毫秒数- 7天的毫秒数= 7天之后那天的毫秒数
        long times;

        while (true){
            if ((startms + sevenTime)>endms){
                String[] time = timelist.split(",");
                String t = "";
                for (int i=0; i<time.length-1; i++){
                    t += time[i];
                }
                timelist = t;
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

    public static void main(String[] args) {
        System.out.println(rDate(new String[]{"2021-1-1", "2021-2-20"}));;
    }
}
