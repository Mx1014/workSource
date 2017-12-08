package com.everhomes.rest.forum;


/**
 * <ul>
 *     <li>FORUM((byte)1): 论坛</li>
 *     <li>ACTIVITY((byte)2): 活动</li>
 *     <li>ANNOUNCEMENT((byte)3): 公告</li>
 *     <li>CLUB((byte)4): 俱乐部</li>
 *     <li>GUILD((byte)5): 行业协会</li>
 *     <li>FEEDBACK((byte)6): 意见反馈</li>
 * </ul>
 */
public enum ForumModuleType {

    FORUM((byte) 1), ACTIVITY((byte) 2), ANNOUNCEMENT((byte) 3), CLUB((byte) 4), GUILD((byte) 5), FEEDBACK((byte) 6);

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
