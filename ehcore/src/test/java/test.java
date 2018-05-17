import com.everhomes.archives.ArchivesUtil;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

public class test {
    @Test
    public void test(){
        /*Long departmentId = 1023329L;
        String path = "/1023080/1023305/1023329";
        System.out.println(path.contains(String.valueOf(departmentId)));

        LocalDate date = LocalDate.now();
        System.out.println(date);
        System.out.println(java.sql.Date.valueOf(date));

        LocalDate nowDate = LocalDate.now();
        System.out.println(nowDate);
        System.out.println(nowDate.plusDays(6));
        System.out.println(LocalDate.now().minusYears(1));

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
        System.out.println(df.format(ArchivesUtil.currentDate().toLocalDate()));
        System.out.println(ArchivesUtil.currentDate().toLocalDate().getDayOfWeek());
        System.out.println(ArchivesUtil.currentDate().toLocalDate().getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, Locale.SIMPLIFIED_CHINESE));

        System.out.println(Locale.SIMPLIFIED_CHINESE.toString());
        System.out.println(formatter.format(ArchivesUtil.currentDate().toLocalDate()));

        System.out.println(0 == departmentId);
        System.out.println(0 == departmentId.longValue());
        Long yep = null;
        System.out.println(0 == yep);
        */
        java.sql.Date date1 = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        java.sql.Date date3 = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        System.out.println(date1);
        System.out.println(date3);
        System.out.println(date3.after(date1));
        System.out.println(date3.equals(date1));
        System.out.println(date3.before(date1));
    }

}
