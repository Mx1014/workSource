// @formatter:off
package com.everhomes.rest.approval;

/**
 * <ul>
 * <li>UN_SUPPORT((byte) 0): 不支持假期余额</li>
 * <li>INACTIVE((byte) 1): 假期余额未启用</li>
 * <li>ACTIVE((byte) 2): 假期余额启用</li>
 * </ul>
 */
public enum ApprovalCategoryReminderFlag {
    UN_SUPPORT((byte) 0), INACTIVE((byte) 1), ACTIVE((byte) 2);

    private byte code;

    ApprovalCategoryReminderFlag(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static ApprovalCategoryReminderFlag fromCode(Byte code) {
        if (code != null) {
            for (ApprovalCategoryReminderFlag flag : ApprovalCategoryReminderFlag.values()) {
                if (flag.getCode() == code.byteValue()) {
                    return flag;
                }
            }
        }
        return null;
    }
}
