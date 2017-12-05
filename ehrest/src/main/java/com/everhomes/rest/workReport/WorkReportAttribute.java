package com.everhomes.rest.workReport;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;

/**
 * <ul>
 * <li>DEFAULT("DEFAULT"): 默认审批 </li>
 * <li>CUSTOMIZE("CUSTOMIZE"): 自定义审批 </li>
 * </ul>
 */
public enum WorkReportAttribute {
    DEFAULT("DEFAULT"), CUSTOMIZE("CUSTOMIZE");

    private String code;

    private WorkReportAttribute(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static WorkReportAttribute fromCode(String code) {
        for (WorkReportAttribute v : WorkReportAttribute.values()) {
            if (StringUtils.equals(v.getCode(), code))
                return v;
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
