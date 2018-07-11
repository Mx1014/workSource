package com.everhomes.archives;

import com.everhomes.rest.archives.*;
import com.everhomes.rest.organization.EmployeeStatus;
import com.everhomes.rest.organization.EmployeeType;
import com.everhomes.rest.organization.MaritalFlag;
import com.everhomes.rest.user.UserGender;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

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

    /**
     * 将文字转化为枚举值
     */
    public static Byte convertToArchivesEnum(String str, String type) {
        if (type.equals(ArchivesParameter.GENDER)) {
            if ("男".equals(str))
                return 1;
            else if ("女".equals(str))
                return 2;
        }

        if (type.equals(ArchivesParameter.MARITAL_FLAG)) {
            if ("已婚".equals(str))
                return 1;
            else if ("未婚".equals(str))
                return 2;
            else if ("离异".equals(str))
                return 3;
            else
                return 0;
        }

        if (type.equals(ArchivesParameter.EMPLOYEE_TYPE)) {
            if ("全职".equals(str))
                return 0;
            else if ("兼职".equals(str))
                return 1;
            else if ("实习".equals(str))
                return 2;
            else if ("劳动派遣".equals(str))
                return 3;
        }

        if (type.equals(ArchivesParameter.EMPLOYEE_STATUS)) {
            if ("试用".equals(str))
                return 0;
            else if ("在职".equals(str))
                return 1;
            else if ("实习".equals(str))
                return 2;
            else
                return 1;
        }
        return null;
    }

    /**
     * 将特定标记符处理为文字信息
     */
    public static String resolveArchivesEnum(Byte value, String type) {
        switch (type) {
            case ArchivesParameter.GENDER:
                if (value == null)
                    return "";
                else if (value == UserGender.MALE.getCode())
                    return "男";
                else if (value == UserGender.FEMALE.getCode())
                    return "女";
                break;
            case ArchivesParameter.MARITAL_FLAG:
                if (value == null)
                    return "";
                else if (value == MaritalFlag.UNDISCLOSURED.getCode())
                    return "保密";
                else if (value == MaritalFlag.MARRIED.getCode())
                    return "已婚";
                else if (value == MaritalFlag.UNMARRIED.getCode())
                    return "未婚";
                else if (value == MaritalFlag.DIVORCE.getCode())
                    return "离异";
                break;
            case ArchivesParameter.EMPLOYEE_TYPE:
                if (value == null)
                    return "";
                else if (value == EmployeeType.FULLTIME.getCode())
                    return "全职";
                else if (value == EmployeeType.PARTTIME.getCode())
                    return "兼职";
                else if (value == EmployeeType.INTERSHIP.getCode())
                    return "实习";
                else if (value == EmployeeType.LABORDISPATCH.getCode())
                    return "劳动派遣";
                break;
            case ArchivesParameter.EMPLOYEE_STATUS:
                if (value == null)
                    return "";
                else if (value == EmployeeStatus.PROBATION.getCode())
                    return "试用";
                else if (value == EmployeeStatus.ON_THE_JOB.getCode())
                    return "在职";
                else if (value == EmployeeStatus.INTERNSHIP.getCode())
                    return "实习";
                else if (value == EmployeeStatus.DISMISSAL.getCode())
                    return "离职";
                break;

            case ArchivesParameter.TRANSFER_TYPE:
                if(value == null)
                    return "";
                else if (value == ArchivesTransferType.PROMOTE.getCode())
                    return "晋升";
                else if (value == ArchivesTransferType.TRANSFER.getCode())
                    return "调整";
                else if (value == ArchivesTransferType.OTHER.getCode())
                    return "其他";
                break;
            case ArchivesParameter.OPERATION_TYPE:
                if (value == ArchivesOperationType.CHECK_IN.getCode())
                    return "入职";
                else if (value == ArchivesOperationType.EMPLOY.getCode())
                    return "转正";
                else if (value == ArchivesOperationType.TRANSFER.getCode())
                    return "调动";
                else if (value == ArchivesOperationType.DISMISS.getCode())
                    return "离职";
            /*case ArchivesParameter.DEPARTMENT:
                List<OrganizationDTO> departments = (List<OrganizationDTO>) obj;
                if (departments != null && departments.size() > 0) {
                    String departmentString = "";
                    for (OrganizationDTO depDTO : departments) {
                        departmentString += depDTO.getName() + ",";
                    }
                    departmentString = departmentString.substring(0, departmentString.length() - 1);
                    return departmentString;
                }
                break;
            case ArchivesParameter.DEPARTMENT_IDS:
                List<Long> ids = (List<Long>) obj;
                String departmentName = "";
                if (ids != null && ids.size() > 0) {
                    for (Long id : ids) {
                        Organization org = organizationProvider.findOrganizationById(id);
                        if (org != null) {
                            departmentName += org.getName() + ",";
                        }
                    }
                    departmentName = departmentName.substring(0, departmentName.length() - 1);
                    return departmentName;
                }
                break;
            case ArchivesParameter.CONTRACT_PARTY_ID:
                if (obj != null) {
                    Long id = (Long) obj;
                    Organization org = organizationProvider.findOrganizationById(id);
                    if (org != null) {
                        return org.getName();
                    }
                }
                break;*/
            case ArchivesParameter.DISMISS_TYPE:
                if (value == ArchivesDismissType.QUIT.getCode())
                    return "辞职";
                else if (value == ArchivesDismissType.FIRE.getCode())
                    return "解雇";
                else if (value == ArchivesDismissType.OTHER.getCode())
                    return "其它";
                else if (value == ArchivesDismissType.RETIRE.getCode())
                    return "退休";
                break;
            case ArchivesParameter.DISMISS_REASON:
                if (value == ArchivesDismissReason.SALARY.getCode())
                    return "薪资";
                else if (value == ArchivesDismissReason.CULTURE.getCode())
                    return "文化";
                else if (value == ArchivesDismissReason.BALANCE.getCode())
                    return "生活平衡";
                else if (value == ArchivesDismissReason.PERSONAL_REASON.getCode())
                    return "个人原因";
                else if (value == ArchivesDismissReason.CAREER_DEVELOPMENT.getCode())
                    return "职业发展";
                else if (value == ArchivesDismissReason.FIRE.getCode())
                    return "不胜任";
                else if (value == ArchivesDismissReason.ADJUSTMENT.getCode())
                    return "编制调整";
                else if (value == ArchivesDismissReason.BREAK_RULE.getCode())
                    return "违纪";
                else if (value == ArchivesDismissReason.OTHER.getCode())
                    return "其他";
                else if (value == ArchivesDismissReason.RETIRE.getCode())
                    return "退休";
                break;
        }
        return "";
    }
}
