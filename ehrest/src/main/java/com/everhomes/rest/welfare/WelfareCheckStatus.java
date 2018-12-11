package com.everhomes.rest.welfare;
/**
 * 
 * <ul>福利状态:
 * <li>SUCESS((byte) 0),EMPLOYEE_RESIGNED((byte) 1),NO_ENOUGH_BALANCE((byte) 2),OTHER((byte) 3)</li>
 * </ul>
 */
public enum WelfareCheckStatus {
    SUCESS((byte) 0),EMPLOYEE_RESIGNED((byte) 1),NO_ENOUGH_BALANCE((byte) 2),OTHER((byte) 3);
    private byte code;

    private WelfareCheckStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static WelfareCheckStatus fromCode(Byte code) {
        WelfareCheckStatus[] values = WelfareCheckStatus.values();
        if(null == code)
        	return null;
        for (WelfareCheckStatus value : values) {
            if (code.equals(value.code)) {
                return value;
            }
        }

        return null;
    }
}
