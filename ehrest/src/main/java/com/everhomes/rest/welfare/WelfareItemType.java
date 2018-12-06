package com.everhomes.rest.welfare;
/**
 * 
 * <ul>福利项类型:
 * <li>MONEY((byte) 0): 钱</li>  
 * </ul>
 */
public enum WelfareItemType {
    MONEY((byte) 0);
    private byte code;

    private WelfareItemType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static WelfareItemType fromCode(Byte code) {
        WelfareItemType[] values = WelfareItemType.values();
        if(null == code)
        	return null;
        for (WelfareItemType value : values) {
            if (code.equals(value.code)) {
                return value;
            }
        }

        return null;
    }
}
