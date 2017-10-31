package com.everhomes.flow.conditionvariable;

import com.everhomes.flow.FlowConditionVariable;

import java.sql.Date;
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
        return variable != null && text.compareTo(variable.getVariable().toString()) == 0;
    }

    @Override
    public boolean isGreaterThen(FlowConditionVariable variable) {
        return variable != null && text.compareTo(variable.getVariable().toString()) > 0;
    }

    @Override
    public boolean isLessThen(FlowConditionVariable variable) {
        return variable != null && text.compareTo(variable.getVariable().toString()) < 0;
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
            float number = Float.parseFloat(text);
            return new FlowConditionNumberVariable(number);
        } catch (NumberFormatException e) {
            // e.printStackTrace();
        }
        return null;
    }
}
