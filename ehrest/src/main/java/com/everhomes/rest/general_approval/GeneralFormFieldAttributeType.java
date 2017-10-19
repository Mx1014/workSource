package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;

/**
 * <ul>
 * <li>DEFAULT("DEFAULT"): 默认字段组(不可删除与修改名称) {@link com.everhomes.rest.general_approval.GeneralFormFieldAttributeType}</li>
 * <li>CUSTOMIZE("CUSTOMIZE"): 自定义</li>
 * </ul>
 */
public enum GeneralFormFieldAttributeType {
    DEFAULT("DEFAULT"), CUSTOMIZE("CUSTOMIZE");

    private String code;

    private GeneralFormFieldAttributeType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static GeneralFormFieldAttributeType fromCode(String code) {
        for (GeneralFormFieldAttributeType v : GeneralFormFieldAttributeType.values()) {
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
