package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>ALL(1): 全部</li>
 *     <li>BOTH(2): 公司和园区都可以审核</li>
 *     <li>ONLY_COMPANY(3): 仅公司审核</li>
 * </ul>
 */
public enum AuditAuth {
    ALL((byte)1), BOTH((byte)2), ONLY_COMPANY((byte)3);

    private Byte code;

    private AuditAuth(Byte code){
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static AuditAuth fromCode(Byte code) {
        if(code != null) {
            AuditAuth[] values = AuditAuth.values();
            for(AuditAuth value : values) {
                if(value.getCode() == code.byteValue()) {
                    return value;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
