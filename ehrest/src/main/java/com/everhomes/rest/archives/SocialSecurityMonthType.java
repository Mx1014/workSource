package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

public enum SocialSecurityMonthType {

    THIS_MONTH((byte) 0), NEXT_MONTH((byte) 1);
    private Byte code;

    private SocialSecurityMonthType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public static SocialSecurityMonthType fromCode(Byte code) {
        if (code != null) {
            for (SocialSecurityMonthType a : SocialSecurityMonthType.values()) {
                if (code.byteValue() == a.getCode().byteValue()) {
                    return a;
                }
            }
        }
        return null;
    }
}
