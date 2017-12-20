// @formatter:off
package com.everhomes.rest.incubator;

/**
 * <ul>
 *     <li>WAIT((byte)0): 待审核</li>
 *     <li>REJECT((byte)1): 驳回</li>
 *     <li>AGREE((byte)2): 通过</li>
 *     <li>CANCEL((byte)3): CANCEL</li>
 * </ul>
 */
public enum ApproveStatus {
    WAIT((byte) 0), REJECT((byte) 1), AGREE((byte) 2), CANCEL((byte) 3);

    private byte code;

    private ApproveStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static ApproveStatus fromCode(Byte code) {
        if (code != null) {
            ApproveStatus[] values = ApproveStatus.values();
            for (ApproveStatus value : values) {
                if (value.code == code.byteValue()) {
                    return value;
                }
            }
        }

        return null;
    }
}
