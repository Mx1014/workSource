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
    public static void main(String[] args) {
        Parent p = new Parent();
        Child c = new Child();
        p.A();
        p.B();
        c.A();
        c.B();
    }
}

class Parent{

    public void A(){
        System.out.println("A");
    }

    public void B(){
        System.out.println("B");
    }
}

class Child extends Parent{

    public void A(){
        super.B();
    }
}
