package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>CURRENT_NODE((byte) 1): 查找当前节点的表单</li>
 * <li>BEFORE_AND_CURRENT_NODE((byte) 2): 查找当前节点及之前的表单</li>
 * </ul>
 */
public enum QueryGeneralFormByFlowNodeType {
    CURRENT_NODE((byte) 1), BEFORE_AND_CURRENT_NODE((byte) 2);

    private byte code;

    QueryGeneralFormByFlowNodeType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static QueryGeneralFormByFlowNodeType fromCode(Byte code) {
        if (code == null) {
            return null;
        }
        for (QueryGeneralFormByFlowNodeType type : QueryGeneralFormByFlowNodeType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
