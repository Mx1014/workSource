import org.junit.Test;

import java.time.LocalDate;

public class test {

    @Test
    public void test(){
        LocalDate date1 = LocalDate.now();
        LocalDate date2 = null;
        java.sql.Date date3 = java.sql.Date.valueOf(date1);
        java.sql.Date  date4 = null;
        date1.isAfter(date2);
//        System.out.println(date2.after(date));
    }
}

