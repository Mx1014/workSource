package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;

/**
 * 工作流节点关联的全局表单字段配置类型
 * @author huqi
 */
public enum GeneralFormFieldsConfigType {
    /**
     * FLOW_NODE_VISIBLE：工作流节点可视化
     */
    FLOW_NODE_VISIBLE("flowNode-visible");

    private String code;

    private GeneralFormFieldsConfigType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static GeneralFormFieldsConfigType fromCode(String code) {
        for (GeneralFormFieldsConfigType v : GeneralFormFieldsConfigType.values()) {
            if (StringUtils.equals(v.getCode(), code)){
                return v;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
