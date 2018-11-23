package com.everhomes.rest.zhenzhihui;

/**
 * <ul>
 * 	<li>PERSON: 1, 个人用户</li>
 *  <li>BOTH: 2，即是个人用户，又是企业用户</li>
 * </ul>
 */
public enum ZhenZhiHuiUserType {
    PERSON((byte) 1), BOTH((byte)2);
    private Byte code;

    ZhenZhiHuiUserType(byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static ZhenZhiHuiUserType fromStringCode(String code) {
        for (ZhenZhiHuiUserType flag : ZhenZhiHuiUserType.values()) {
            if (flag.name().equalsIgnoreCase(code)) {
                return flag;
            }
        }
        return PERSON;
    }

    public static ZhenZhiHuiUserType fromCode(Byte code) {
    	if(null == code){
    		return null;
    	}
        for (ZhenZhiHuiUserType flag : ZhenZhiHuiUserType.values()) {
            if (flag.code == code.byteValue()) {
                return flag;
            }
        }
        return null;
    }
}
