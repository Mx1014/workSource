package com.everhomes.rest.activity;
/**
 * <ul>
 * 	<li>SIGNUP: 0, 不收费</li>
 *  <li>CHARGE: 1， 收费</li>
 * </ul>
 */
public enum ActivitySignupFlag {
	UNSIGNUP((byte) 0), SIGNUP((byte) 1);
    private Byte code;

    ActivitySignupFlag(byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static ActivitySignupFlag fromStringCode(String code) {
        for (ActivitySignupFlag flag : ActivitySignupFlag.values()) {
            if (flag.name().equalsIgnoreCase(code)) {
                return flag;
            }
        }
        return UNSIGNUP;
    }

    public static ActivitySignupFlag fromCode(Byte code) {
    	if(null == code){
    		return null;
    	}
        for (ActivitySignupFlag flag : ActivitySignupFlag.values()) {
            if (flag.code == code.byteValue()) {
                return flag;
            }
        }
        return null;
    }
}
