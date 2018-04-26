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
//        System.out.println(0 == yep.longValue());*/
//        System.out.println(LocalDateTime.now().getDayOfWeek().getValue());

        ZoneId zoneId = ZoneId.systemDefault();
//        LocalDateTime nowDateTime = LocalDateTime.now();
        LocalDateTime dateTime2 = LocalDateTime.of(2018,4,13,23,50);
        System.out.println("++++++++++++++++++DATETIME2: " + dateTime2);
//        LocalDateTime nextDateTime = LocalDateTime.of(dateTime2.getYear(), dateTime2.getMonthValue(), dateTime2.getDayOfMonth(), dateTime2.getHour() +1, 0);
//        System.out.println("++++++++++++++++++NEXTDATETIME: " + nextDateTime);
//        ZonedDateTime zdt = nextDateTime.atZone(zoneId);
//        Date date = Date.from(zdt.toInstant());
//        System.out.println("++++++++++++++++++DATE: " + date);

        LocalDateTime dateTime3 = dateTime2.plusHours(1);
        System.out.println("++++++++++++++++++DATETIME3: " + dateTime3);
        dateTime3 = LocalDateTime.of(dateTime3.getYear(), dateTime3.getMonthValue(), dateTime3.getDayOfMonth(), dateTime3.getHour(), 0);
        System.out.println("++++++++++++++++++DATETIME3: " + dateTime3);
        ZonedDateTime zdt2 = dateTime3.atZone(zoneId);
        Date date2 = Date.from(zdt2.toInstant());
        System.out.println("++++++++++++++++++DATE2: " + date2);

        java.sql.Date date1 = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        java.sql.Date date3 = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        System.out.println(date3.after(date1));
    }

}
