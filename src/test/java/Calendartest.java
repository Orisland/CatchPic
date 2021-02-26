import java.util.Calendar;
import java.util.Date;

public class Calendartest {
    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date((2021-1900),1,28));
        calendar.add(Calendar.DATE, 7);
        Date date = calendar.getTime();
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String s = format.format(date);
        System.out.println(s);
    }
}
