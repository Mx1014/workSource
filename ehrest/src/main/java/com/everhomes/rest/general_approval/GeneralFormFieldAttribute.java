package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;

/**
 * <ul>
 * <li>DEFAULT("DEFAULT"): 默认字段组 </li>
 * <li>CUSTOMIZE("CUSTOMIZE"): 自定义</li>
 * </ul>
 */
public enum GeneralFormFieldAttribute {
    DEFAULT("DEFAULT"), CUSTOMIZE("CUSTOMIZE");

    private String code;

    private GeneralFormFieldAttribute(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static GeneralFormFieldAttribute fromCode(String code) {
        for (GeneralFormFieldAttribute v : GeneralFormFieldAttribute.values()) {
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
