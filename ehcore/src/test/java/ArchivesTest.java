import com.everhomes.rest.archives.ArchivesUtil;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ArchivesTest {
    String str = "1506859877968";

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

    @Test
    public void testDrive(){
        String str = "2017-09-20";
        java.sql.Date date = ArchivesUtil.currentDate();
        java.sql.Date dateStr = ArchivesUtil.parseDate(str);
        System.out.println(dateStr);
        System.out.println(date);
        System.out.println(date.toString().equals(dateStr.toString()));
    }
}
