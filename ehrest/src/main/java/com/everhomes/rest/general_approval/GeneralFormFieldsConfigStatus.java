package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * @author huqi
 */
public enum GeneralFormFieldsConfigStatus {
    /**
     * INVALID(0)-无效，CONFIG(1)-已配置，RUNNING(2)-运行中
     */
    INVALID((byte)0), CONFIG((byte)1), RUNNING((byte)2);

    private byte code;

    private GeneralFormFieldsConfigStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static GeneralFormFieldsConfigStatus fromCode(byte code) {
        for (GeneralFormFieldsConfigStatus v : GeneralFormFieldsConfigStatus.values()) {
            if (v.getCode() == code){
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
