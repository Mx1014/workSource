package com.everhomes.flow.conditionvariable;

import com.everhomes.flow.FlowConditionVariable;

import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * Created by xq.tian on 2017/11/2.
 */
public class FlowConditionStringVariable implements FlowConditionVariable<String> {

    private String text;

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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
            TemporalAccessor accessor = formatter.parse(text);
            LocalDateTime dateTime = LocalDateTime.from(accessor);
            return new FlowConditionDateVariable(Date.from(dateTime.toInstant(ZoneOffset.UTC)));
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return null;
    }

    public FlowConditionNumberVariable toNumberVariable() {
        try {
            Float number = Float.valueOf(text);
            return new FlowConditionNumberVariable(number);
        } catch (NumberFormatException e) {
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
