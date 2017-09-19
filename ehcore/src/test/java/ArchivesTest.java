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
        Echo e1 = new Echo();
        Echo e2 = e1;
        int x = 0;
        while(x<4){
            e1.hello();
            e1.count += 1;
            if(x==3){
                e2.count = e2.count+1;
            }
            if(x > 0){
                e2.count = e2.count + e1.count;
            }
            x = x+1;
        }
        System.out.println(e2.count);
    }
}

class Echo{
    int count = 0;
    void hello(){
        System.out.println("helloooo... ");
    }
}
