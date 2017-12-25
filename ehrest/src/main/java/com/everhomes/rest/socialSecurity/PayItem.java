package com.everhomes.rest.socialSecurity;

/**
 * <ul>
 * <li>PENSION("养老")</li>
 * <li>MEDICAL("医疗")</li>
 * <li>BIRTH("生育")</li>
 * <li>UNEMPLOYMENT("失业")</li>
 * <li>INJURY("工伤")</li>
 * </ul>
 */
public enum PayItem {

	PENSION("养老"),
	MEDICAL("医疗"),
	BIRTH("生育"),
	UNEMPLOYMENT("失业"),
	INJURY("工伤");

    private String code;

    private PayItem(String code ) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PayItem fromCode(String code) {
        for(PayItem t : PayItem.values()) {
            if (code != null && code.equals(t.code)) {
                return t;
            }
        }
        return null;
    }


}
