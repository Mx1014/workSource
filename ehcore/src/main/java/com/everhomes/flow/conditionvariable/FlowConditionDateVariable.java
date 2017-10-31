package com.everhomes.flow.conditionvariable;

import com.everhomes.flow.FlowConditionVariable;

import java.util.Date;

/**
 * Created by xq.tian on 2017/11/2.
 */
public class FlowConditionDateVariable implements FlowConditionVariable<Date> {

    private Date date;

    public FlowConditionDateVariable(Date date) {
        this.date = date;
    }

    @Override
    public Date getVariable() {
        return date;
    }

    @Override
    public boolean isEqual(FlowConditionVariable variable) {
        return doCompare(this, variable, (date1, date2) -> date1.compareTo(date2) == 0);
    }

    @Override
    public boolean isGreaterThen(FlowConditionVariable variable) {
        return doCompare(this, variable, (date1, date2) -> date1.compareTo(date2) > 0);
    }

    @Override
    public boolean isLessThen(FlowConditionVariable variable) {
        return doCompare(this, variable, (date1, date2) -> date1.compareTo(date2) < 0);
    }

    interface CompareCallback {
        boolean compare(Date date1, Date date2);
    }

    private boolean doCompare(FlowConditionDateVariable variable1, FlowConditionVariable variable2, CompareCallback callback) {
        if (variable2 instanceof FlowConditionDateVariable) {
            return callback.compare(variable1.getVariable(), ((FlowConditionDateVariable) variable2).getVariable());
        } else if (variable2 instanceof FlowConditionStringVariable) {
            FlowConditionDateVariable dateVariable = ((FlowConditionStringVariable) variable2).toDateVariable();
            if (dateVariable != null) {
                return callback.compare(variable1.getVariable(), dateVariable.getVariable());
            }
        }
        return false;
    }
}
