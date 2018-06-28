package com.everhomes.rest.archives;

/**
 * <ul>
 * <li>SALARY((byte) 0): 薪资</li>
 * <li>CULTURE((byte) 1): 文化</li>
 * <li>BALANCE((byte) 2): 生活平衡</li>
 * <li>PERSONAL_REASON((byte) 3): 个人原因</li>
 * <li>CAREER_DEVELOPMENT((byte) 4): 职业发展</li>
 * <li>FIRE((byte) 5): 不胜任</li>
 * <li>ADJUSTMENT((byte) 6): 编制调整</li>
 * <li>BREAK_RULE((byte) 7): 违纪</li>
 * <li>RETIRE((byte) 9): 退休</li>
 * <li>OTHER((byte) 8): 其他</li>
 * </ul>
 */
public enum ArchivesDismissReason {
    SALARY((byte) 0, "薪资"), CULTURE((byte) 1, "文化"), BALANCE((byte) 2, "生活平衡"), PERSONAL_REASON((byte) 3, "个人原因"), CAREER_DEVELOPMENT((byte) 4, "职业发展"),
    FIRE((byte) 5, "不胜任"), ADJUSTMENT((byte) 6, "编制调整"), BREAK_RULE((byte) 7, "违纪"),RETIRE((byte) 9, "退休"), OTHER((byte) 8, "其他");

    private Byte code;
    private String type;

    private ArchivesDismissReason(Byte code, String type) {
        this.code = code;
        this.type = type;
    }

    public byte getCode() {
        return this.code;
    }

    public String getType(){
        return this.type;
    }

    public static ArchivesDismissReason fromCode(Byte code) {
        if(code != null) {
            ArchivesDismissReason[] values = ArchivesDismissReason.values();
            for(ArchivesDismissReason value : values) {
                if(code.byteValue() == value.code) {
                    return value;
                }
            }
        }
        return null;
    }
}