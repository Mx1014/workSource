package com.everhomes.archives;

import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.archives.ArchivesDismissReason;
import com.everhomes.rest.archives.ArchivesDismissType;
import com.everhomes.rest.archives.ArchivesParameter;
import com.everhomes.rest.general_approval.GeneralFormFieldAttribute;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.organization.EmployeeStatus;
import com.everhomes.rest.organization.EmployeeType;
import com.everhomes.rest.organization.MaritalFlag;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.user.UserGender;
import org.springframework.util.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArchivesUtil {

    /**
     * String 转 java.sql.Date
     */
    public static java.sql.Date parseDate(String strDate) {
        String pattern = null;
        if (strDate != null) {
            if (strDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                pattern = "yyyy-MM-dd";
            } else if (strDate.matches("\\d{2}/\\d{2}/\\d{2}")) {
                pattern = "MM/dd/yy";
            }else if (strDate.matches("\\d{2}/\\d/\\d{2}")) {
                pattern = "MM/d/yy";
            }else if (strDate.matches("\\d/\\d/\\d{2}")) {
                pattern = "M/d/yy";
            }else if (strDate.matches("\\d/\\d{2}/\\d{2}")) {
                pattern = "M/dd/yy";
            }
        }
        if (pattern == null)
            return null;
        TemporalAccessor accessor = DateTimeFormatter.ofPattern(pattern).parse(strDate);
//        DateTimeFormatter formatter = (DateTimeFormatter) accessor;
//        LocalDateTime localDateTime = LocalDateTime.parse(strDate, formatter);
//        java.util.Date d = java.util.Date.from(localDateTime.atZone(zone).toInstant());
        LocalDate date = LocalDate.from(accessor);
        return java.sql.Date.valueOf(date);
    }

    /**
     * 当前日期(sql.Date 类型)
     */
    public static java.sql.Date currentDate(){
        LocalDate date = LocalDate.now();
        return java.sql.Date.valueOf(date);
    }

    /**
     * 日期+n天
     */
    public static java.sql.Date plusDate(java.sql.Date date, int n){
        LocalDate resultDate = date.toLocalDate().plusDays(n);
        return java.sql.Date.valueOf(resultDate);
    }

    /**
     * 当前日期一年期
     */
    public static java.sql.Date previousYear(java.sql.Date date){
        LocalDate previousYear = date.toLocalDate().minusYears(1);
        return java.sql.Date.valueOf(previousYear);
    }

    /**
     * 本周起始日
     */
    public static java.sql.Date firstOfWeek(){
        LocalDate nowDate = LocalDate.now();
        LocalDate date = nowDate.minusDays(nowDate.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());
        return java.sql.Date.valueOf(date);
    }

    /**
     * 本周截止日
     */
    public static java.sql.Date lastOfWeek(){
        LocalDate nowDate = LocalDate.now();
        LocalDate date = nowDate.plusDays(DayOfWeek.SUNDAY.getValue() - nowDate.getDayOfWeek().getValue());
        return java.sql.Date.valueOf(date);
    }

    /**
     * 获取月份与日期(MMdd格式)
     */
    public static String getMonthAndDay(java.sql.Date date) {
        if (date == null)
            return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
        return formatter.format(date.toLocalDate());
    }

    /**
     * 数字与26字母的转换
     * (1-A,2-B...)
     */
    public static String GetExcelLetter(int n) {
        String s = "";
        while (n > 0) {
            int m = n % 26;
            if (m == 0)
                m = 26;
            s = (char) (m + 64) + s;
            n = (n - m) / 26;
        }
        return s;
    }
}
