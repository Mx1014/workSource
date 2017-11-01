package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;

/**
 * <ul>
 * <li>DEFAULT("DEFAULT"): 默认审批 </li>
 * <li>CUSTOMIZE("CUSTOMIZE"): 自定义审批 </li>
 * <li>ASK_FOR_LEAVE("ASK_FOR_LEAVE"): 请假审批</li>
 * <li>BUSINESS_TRIP("BUSINESS_TRIP"): 出差审批</li>
 * <li>OVERTIME("OVERTIME"): 加班审批</li>
 * <li>GO_OUT("GO_OUT"): 外出审批</li>
 * <li>ABNORMAL_PUNCH("ABNORMAL_PUNCH"): 打卡异常审批</li>
 * </ul>
 */
public enum GeneralApprovalAttribute {
    DEFAULT("DEFAULT"), CUSTOMIZE("CUSTOMIZE"), ASK_FOR_LEAVE("ASK_FOR_LEAVE"), CANCEL_FOR_LEAVE("CANCEL_FOR_LEAVE"), BUSINESS_TRIP("BUSINESS_TRIP"),
    OVERTIME("OVERTIME"), GO_OUT("GO_OUT"), ABNORMAL_PUNCH("ABNORMAL_PUNCH");

    private String code;

    private GeneralApprovalAttribute(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static GeneralApprovalAttribute fromCode(String code) {
        for (GeneralApprovalAttribute v : GeneralApprovalAttribute.values()) {
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
