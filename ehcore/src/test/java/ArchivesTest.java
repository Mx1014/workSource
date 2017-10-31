import com.everhomes.rest.archives.ArchivesUtil;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ArchivesTest {
    String str = "1506859877968";

    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");


    public void test() {
/*        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try{
            Timestamp time = Timestamp.valueOf(str);
            System.out.println(time);
        }catch (Exception e){
            System.out.println("bug");
        }
    }*/
        System.out.println(ArchivesUtil.currentDate());

    }

    public void testDrive(){/*
        String str = "2017-09-20";
        java.sql.Date date = ArchivesUtil.currentDate();
        java.sql.Date dateStr = ArchivesUtil.parseDate(str);
        System.out.println(dateStr);
        System.out.println(date);
        System.out.println(date.toString().equals(dateStr.toString()));*/
        System.out.println(ArchivesUtil.currentDate());
    }

    public void testWeek(){
/*        Calendar c = Calendar.getInstance();
        System.out.println(c.get(Calendar.DAY_OF_WEEK));
        int i = 7;
        byte j = 7;
        System.out.println(j==i);*/

    }
/*    public void testDate(){
        java.util.Date now = new java.util.Date();
        System.out.println(format.format(now));

        String r = "123";
        String c = "12";
        for(int i=0; i<4-c.length(); i++){
            r += "0";
        }
        r += c;
        System.out.println(r);

        Long a = null;
        System.out.println(String.valueOf(a));
    }*/
    @Test
    public void testToken(){
        String num1 = "+86 123456";
        String num2 = "123456";
        String token1[] = num1.split(" ");
        String token2[] = num2.split(" ");
        System.out.println(token1.length + " : " + token1.toString());
        System.out.println(token2.length + " : " + token2.toString());
    }
}
