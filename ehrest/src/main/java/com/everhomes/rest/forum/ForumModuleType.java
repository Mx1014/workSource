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

    FORUM((byte) 1, (long)10100), ACTIVITY((byte) 2, (long)10600), ANNOUNCEMENT((byte) 3, (long)10300),
    CLUB((byte) 4, (long)10750), GUILD((byte) 5, (long)10760), FEEDBACK((byte) 6, null);

    private Byte code;

    private Long moduleTypeId;

    private ForumModuleType(Byte code, Long moduleTypeId) {
        this.code = code;
        this.moduleTypeId = moduleTypeId;
    }

    public Byte getCode() {
        return this.code;
    }

    public Long getModuleTypeId() {
        return this.moduleTypeId;
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

    public Long getModuleId(){
        switch (this){
            case FORUM:
            case ANNOUNCEMENT:
                return 10100L;
            case ACTIVITY:
                return 10600L;
            case CLUB:
                return 10750L;
            case GUILD:
                return 10760L;
            case FEEDBACK:
                return null;
            default:
                return null;
        }
    }

}
