// @formatter:off
package com.everhomes.rest.approval;

/**
 * <ul>请假类型的状态：
 * <li>DELETED((byte) 0): 删除状态</li>
 * <li>INACTIVE((byte) 1): 未启用</li>
 * <li>ACTIVE((byte) 2): 启用</li>
 * <li>ACTIVE_FOREVER((byte)3): 永久启用（不可禁用）</li>
 * </ul>
 */
public enum ApprovalCategoryStatus {
    DELETED((byte) 0), INACTIVE((byte) 1), ACTIVE((byte) 2), ACTIVE_FOREVER((byte) 3);

    private byte code;

    ApprovalCategoryStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static ApprovalCategoryStatus fromCode(Byte code) {
        if (code != null) {
            for (ApprovalCategoryStatus status : ApprovalCategoryStatus.values()) {
                if (status.getCode() == code.byteValue()) {
                    return status;
                }
            }
        }
        return null;
    }
}
