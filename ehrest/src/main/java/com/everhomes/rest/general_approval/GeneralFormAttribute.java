package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;

/**
 * <ul>
 * <li>DEFAULT("DEFAULT"): 系统表单 </li>
 * <li>CUSTOMIZE("CUSTOMIZE"): 自定义表单</li>
 * </ul>
 */
public enum GeneralFormAttribute {
    DEFAULT("DEFAULT"), CUSTOMIZE("CUSTOMIZE");

    private String code;

    private GeneralFormAttribute(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static GeneralFormAttribute fromCode(String code) {
        for (GeneralFormAttribute v : GeneralFormAttribute.values()) {
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
