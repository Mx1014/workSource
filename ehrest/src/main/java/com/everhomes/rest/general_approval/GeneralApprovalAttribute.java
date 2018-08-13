package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;

/**
 * <ul>
 * <li>DEFAULT("DEFAULT"): 默认审批 </li>
 * <li>CUSTOMIZE("CUSTOMIZE"): 自定义审批 </li>
 * <li>ASK_FOR_LEAVE("ASK_FOR_LEAVE"): 请假申请</li>
 * <li>BUSINESS_TRIP("BUSINESS_TRIP"): 出差申请</li>
 * <li>OVERTIME("OVERTIME"): 加班申请</li>
 * <li>GO_OUT("GO_OUT"): 外出申请</li>
 * <li>ABNORMAL_PUNCH("ABNORMAL_PUNCH"): 异常申请</li>
 * <li>EMPLOY_APPLICATION("EMPLOY_APPLICATION"): 转正申请</li>
 * <li>DISMISS_APPLICATION("DISMISS_APPLICATION"): 离职申请</li>
 * </ul>
 */
public enum GeneralApprovalAttribute {
    DEFAULT("DEFAULT"), CUSTOMIZE("CUSTOMIZE"), ASK_FOR_LEAVE("ASK_FOR_LEAVE"), CANCEL_FOR_LEAVE("CANCEL_FOR_LEAVE"), BUSINESS_TRIP("BUSINESS_TRIP"),
    OVERTIME("OVERTIME"), GO_OUT("GO_OUT"), ABNORMAL_PUNCH("ABNORMAL_PUNCH"), EMPLOY_APPLICATION("EMPLOY_APPLICATION"), DISMISS_APPLICATION("DISMISS_APPLICATION");

    private String code;

    GeneralApprovalAttribute(String code) {
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
