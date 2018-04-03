// @formatter:off
package com.everhomes.rest.incubator;

/**
 * <ul>
 *     <li>BUSINESS_LICENCE((byte)1): 营业执照扫描件</li>
 *     <li>PLAN_BOOK((byte)2): 创业计划书</li>
 * </ul>
 */
public enum IncubatorApplyAttachmentType {
    BUSINESS_LICENCE((byte) 1), PLAN_BOOK((byte) 2);

    private byte code;

    private IncubatorApplyAttachmentType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static IncubatorApplyAttachmentType fromCode(Byte code) {
        if (code != null) {
            IncubatorApplyAttachmentType[] values = IncubatorApplyAttachmentType.values();
            for (IncubatorApplyAttachmentType value : values) {
                if (value.code == code.byteValue()) {
                    return value;
                }
            }
        }

        return null;
    }
}
