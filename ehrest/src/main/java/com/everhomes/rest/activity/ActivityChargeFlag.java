package com.everhomes.rest.activity;
/**
 * <ul>
 * 	<li>UNCHARGE: 0, 不收费</li>
 *  <li>CHARGE: 1， 收费</li>
 * </ul>
 */
public enum ActivityChargeFlag {
    UNCHARGE((byte) 0), CHARGE((byte) 1);
    private Byte code;

    ActivityChargeFlag(byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static ActivityChargeFlag fromStringCode(String code) {
        for (ActivityChargeFlag flag : ActivityChargeFlag.values()) {
            if (flag.name().equalsIgnoreCase(code)) {
                return flag;
            }
        }
        return UNCHARGE;
    }

    public static ActivityChargeFlag fromCode(Byte code) {
    	if(null == code){
    		return null;
    	}
        for (ActivityChargeFlag flag : ActivityChargeFlag.values()) {
            if (flag.code == code.byteValue()) {
                return flag;
            }
        }
        return null;
    }
}
