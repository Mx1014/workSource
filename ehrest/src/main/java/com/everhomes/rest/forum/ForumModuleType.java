package com.everhomes.rest.forum;


/**
 *<ul>
 *<li>ACTIVITY("activity"):活动</li>
 *<li>TOPIC("topic"):活动</li>
 *<li>POLL("poll"):活动</li>
 *</ul>
 */
public enum ForumModuleType {

    FORUM((byte)1), ACTIVITY((byte)2), CLUB((byte)3), GUILD((byte)4), FEEDBACK((byte)5);

    private Byte code;
    private ForumModuleType(Byte code) {
        this.code = code;
    }
    
    public Byte getCode() {
        return this.code;
    }

    public static ForumModuleType fromCode(Byte code) {
        if (code != null) {
            ForumModuleType[] values = ForumModuleType.values();
            for (ForumModuleType value : values) {
                if (code.equals(value.code)) {
                    return value;
                }
            }
        }
        return null;
    }

}
