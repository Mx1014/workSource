import org.junit.Test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ArchivesTest {
    String str = "1506859877968";

    @Test
    public void test(){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try{
            Timestamp time = Timestamp.valueOf(str);
            System.out.println(time);
        }catch (Exception e){
            System.out.println("bug");
        }
    }
}
