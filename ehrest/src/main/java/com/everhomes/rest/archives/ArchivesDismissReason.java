package com.everhomes.rest.archives;

/**
 * <ul>
 * <li>SALARY("薪资"): 薪资</li>
 * <li>CULTURE("文化"): 文化</li>
 * <li>BALANCE("生活平衡"): 生活平衡</li>
 * <li>PERSONALREASON("个人原因"): 个人原因</li>
 * <li>CAREERDEVELOPMENT("职业发展"): 职业发展</li>
 * <li>FIRE("不胜任"): 不胜任</li>
 * <li>ADJUSTMENT("编制调整"): 编制调整</li>
 * <li>BREAKRULE("违纪"): 违纪</li>
 * <li>OTHER("其他"): 其他</li>
 * </ul>
 */
public enum ArchivesDismissReason {
    SALARY("薪资"), CULTURE("文化"), BALANCE("生活平衡"), PERSONALREASON("个人原因"), CAREERDEVELOPMENT("职业发展"),
    FIRE("不胜任"), ADJUSTMENT("编制调整"), BREAKRULE("违纪"), OTHER("其他");

    private String code;

    private ArchivesDismissReason(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static ArchivesDismissReason fromCode(String code) {
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