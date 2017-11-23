package com.everhomes.flow.conditionvariable;

import com.everhomes.flow.FlowConditionVariable;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by xq.tian on 2017/11/2.
 */
public class FlowConditionStringVariable implements FlowConditionVariable<String> {

    private String text;
    private static final String DATE_TIME_REGEX_SLASH = "\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{1,2}";
    private static final String DATE_TIME_REGEX_CENTER = "\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}";
    private static final String DATE_REGEX_CENTER = "\\d{4}-\\d{1,2}-\\d{1,2}";
    private static final String DATE_REGEX_SLASH = "\\d{4}/\\d{1,2}/\\d{1,2}";

    @Override
    public String getVariable() {
        return text;
    }

    public FlowConditionStringVariable(String text) {
        this.text = text;
    }

    @Override
    public boolean isEqual(FlowConditionVariable variable) {
        return variable != null && doCompare(this, variable, new CompareCallback() {
            @Override
            public boolean compare(String str1, String str2) {
                return str1.equals(str2);
            }

            @Override
            public boolean compareNumber(Number number1, Number number2) {
                return Math.abs(number1.floatValue() - number2.floatValue()) <= 0.00001;
            }

            @Override
            public boolean compareDate(Date date1, Date date2) {
                return date1.equals(date2);
            }
        });
    }

    @Override
    public boolean isGreaterThen(FlowConditionVariable variable) {
        return variable != null && doCompare(this, variable, new CompareCallback() {
            @Override
            public boolean compare(String str1, String str2) {
                return str1.compareTo(str2) > 0;
            }

            @Override
            public boolean compareNumber(Number number1, Number number2) {
                return number1.floatValue() - number2.floatValue() > 0;
            }

            @Override
            public boolean compareDate(Date date1, Date date2) {
                return date1.after(date2);
            }
        });
    }

    @Override
    public boolean isLessThen(FlowConditionVariable variable) {
        return variable != null && doCompare(this, variable, new CompareCallback() {
            @Override
            public boolean compare(String str1, String str2) {
                return str1.compareTo(str2) > 0;
            }

            @Override
            public boolean compareNumber(Number number1, Number number2) {
                return number1.floatValue() - number2.floatValue() < 0;
            }

            @Override
            public boolean compareDate(Date date1, Date date2) {
                return date1.before(date2);
            }
        });
    }

    private boolean doCompare(FlowConditionStringVariable variable1, FlowConditionVariable variable2, CompareCallback callback) {
        if (variable2 instanceof FlowConditionStringVariable) {
            return callback.compare(variable1.getVariable(), ((FlowConditionStringVariable) variable2).getVariable());
        } else if (variable2 instanceof FlowConditionNumberVariable) {
            FlowConditionNumberVariable numberVariable = this.toNumberVariable();
            if (numberVariable != null) {
                return callback.compareNumber(numberVariable.getVariable(), ((FlowConditionNumberVariable) variable2).getVariable());
            }
        } else if (variable2 instanceof FlowConditionDateVariable) {
            FlowConditionDateVariable dateVariable = this.toDateVariable();
            if (dateVariable != null) {
                return callback.compareDate(dateVariable.getVariable(), ((FlowConditionDateVariable) variable2).getVariable());
            }
        }
        return false;
    }

    public FlowConditionDateVariable toDateVariable() {
        try {
            String temp = text;
            String pattern;
            if (temp.matches(DATE_TIME_REGEX_SLASH)) {
                pattern = "yyyy/MM/dd HH:mm:ss";
                temp += ":00";
            } else if (temp.matches(DATE_TIME_REGEX_CENTER)) {
                pattern = "yyyy-MM-dd HH:mm:ss";
                temp += ":00";
            } else if (temp.matches(DATE_REGEX_SLASH)) {
                pattern = "yyyy/MM/dd HH:mm:ss";
                temp += " 00:00:00";
            } else if (temp.matches(DATE_REGEX_CENTER)) {
                pattern = "yyyy-MM-dd HH:mm:ss";
                temp += " 00:00:00";
            } else {
                pattern = "yyyy/MM/dd HH:mm:ss";
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime dateTime = LocalDateTime.parse(temp, formatter);
            return new FlowConditionDateVariable(Date.from(dateTime.toInstant(OffsetDateTime.now().getOffset())));
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return null;
    }

    /*public static void main(String[] args) {
        String text = "2017/11/11";
        String pattern;
        if (text.matches(DATE_TIME_REGEX_SLASH)) {
            pattern = "yyyy/MM/dd HH:mm:ss";
            text += ":00";
        } else if (text.matches(DATE_TIME_REGEX_CENTER)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
            text += ":00";
        } else if (text.matches(DATE_REGEX_SLASH)) {
            pattern = "yyyy/MM/dd HH:mm:ss";
            text += " 00:00:00";
        } else if (text.matches(DATE_REGEX_CENTER)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
            text += " 00:00:00";
        } else {
            pattern = "yyyy/MM/dd HH:mm:ss";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime dateTime = LocalDateTime.parse(text, formatter);
        System.out.println(Date.from(dateTime.toInstant(OffsetDateTime.now().getOffset())).toLocaleString());
    }*/

    public FlowConditionNumberVariable toNumberVariable() {
        try {
            Float number = Float.valueOf(text);
            return new FlowConditionNumberVariable(number);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return null;
    }

    interface CompareCallback {
        boolean compare(String str1, String str2);

        boolean compareNumber(Number number1, Number number2);

        boolean compareDate(Date date1, Date date2);
    }
}
