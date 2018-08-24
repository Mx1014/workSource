package com.everhomes.rest.zhenzhihui;

/**
 * <ul>
 * 	<li>PERSON: 1, 个人办事</li>
 *  <li>ENTERPRISE: 2，企业办事</li>
 * </ul>
 */
public enum ZhenZhiHuiAffairType {
    PERSON(1001), ENTERPRISE(1002);
    private Integer code;

    ZhenZhiHuiAffairType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static ZhenZhiHuiAffairType fromStringCode(String code) {
        for (ZhenZhiHuiAffairType flag : ZhenZhiHuiAffairType.values()) {
            if (flag.name().equalsIgnoreCase(code)) {
                return flag;
            }
        }
        return PERSON;
    }

    public static ZhenZhiHuiAffairType fromCode(Byte code) {
    	if(null == code){
    		return null;
    	}
        for (ZhenZhiHuiAffairType flag : ZhenZhiHuiAffairType.values()) {
            if (flag.code == code.byteValue()) {
                return flag;
            }
        }
        return null;
    }
}
