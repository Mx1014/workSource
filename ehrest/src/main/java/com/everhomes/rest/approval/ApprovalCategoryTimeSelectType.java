// @formatter:off
package com.everhomes.rest.approval;

/**
 * <ul>
 * <li>FIRST_HALF_BEGIN((byte) 0): 上半天（开始）</li>
 * <li>FIRST_HALF_END((byte) 1): 上半天（结束）</li>
 * <li>SECOND_HALF_BEGIN((byte) 2): 下半天（开始）</li>
 * <li>SECOND_HALF_END((byte) 3): 下半天（结束）</li>
 * </ul>
 */
public enum ApprovalCategoryTimeSelectType {
    FIRST_HALF_BEGIN((byte) 0), FIRST_HALF_END((byte) 1), SECOND_HALF_BEGIN((byte) 2), SECOND_HALF_END((byte) 3);

    private byte code;

    ApprovalCategoryTimeSelectType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static ApprovalCategoryTimeSelectType fromCode(Byte code) {
        if (code != null) {
            for (ApprovalCategoryTimeSelectType type : ApprovalCategoryTimeSelectType.values()) {
                if (type.getCode() == code.byteValue()) {
                    return type;
                }
            }
        }
        return null;
    }
}
