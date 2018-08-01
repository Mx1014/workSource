package com.everhomes.techpark.punch;

import com.everhomes.rest.techpark.punch.PunchDayType;
import com.everhomes.server.schema.tables.pojos.EhPunchTimeRules;
import com.everhomes.util.StringHelper;

public class PunchTimeRule extends EhPunchTimeRules {
    /**
     * @author Wuhan
     */
    private static final long serialVersionUID = 3038821225471012801L;

    private PunchDayType punchDayType;
    // 是排班，但未安排班次
    private Byte unscheduledFlag;

    public PunchTimeRule() {
        setPunchDayType(PunchDayType.WORKDAY);
    }

    public PunchTimeRule(Long id, PunchDayType type) {
        setId(id);
        setPunchDayType(type);
    }

    public PunchDayType getPunchDayType() {
        return punchDayType;
    }

    public void setPunchDayType(PunchDayType punchDayType) {
        this.punchDayType = punchDayType;
    }

    public Byte getUnscheduledFlag() {
        return unscheduledFlag;
    }

    public void setUnscheduledFlag(Byte unscheduledFlag) {
        this.unscheduledFlag = unscheduledFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
