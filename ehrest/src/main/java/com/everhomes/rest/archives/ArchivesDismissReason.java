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
 * <li>OTHER((byte) 8): 其他</li>
 * </ul>
 */
public enum ArchivesDismissReason {
    SALARY((byte) 0), CULTURE((byte) 1), BALANCE((byte) 2), PERSONAL_REASON((byte) 3), CAREER_DEVELOPMENT((byte) 4),
    FIRE((byte) 5), ADJUSTMENT((byte) 6), BREAK_RULE((byte) 7), OTHER((byte) 8);

    private Byte code;

    private ArchivesDismissReason(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static ArchivesDismissReason fromCode(Byte code) {
        if (code != null) {
            ArchivesDismissReason[] values = ArchivesDismissReason.values();
            for (ArchivesDismissReason value : values) {
                if (code.equals(value.code)) {
                    return value;
                }
            }
        }

        return null;
    }
}