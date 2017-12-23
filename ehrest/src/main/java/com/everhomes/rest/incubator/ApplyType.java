// @formatter:off
package com.everhomes.rest.incubator;

/**
 * <ul>
 *     <li>INCUBATOR((byte)0): 入孵申请</li>
 *     <li>GROWING((byte)1): 加速</li>
 *     <li>COMMUNITY((byte)2): 入园</li>
 * </ul>
 */
public enum ApplyType {
    INCUBATOR((byte) 0, "入孵申请"), GROWING((byte) 1, "加速申请"), COMMUNITY((byte) 2, "入园申请");

    private byte code;
    private String text;

    private ApplyType(byte code, String text) {
        this.code = code;
        this.text = text;
    }

    public byte getCode() {
        return this.code;
    }

    public String getText(){
        return this.text;
    }

    public static ApplyType fromCode(Byte code) {
        if (code != null) {
            ApplyType[] values = ApplyType.values();
            for (ApplyType value : values) {
                if (value.code == code.byteValue()) {
                    return value;
                }
            }
        }

        return null;
    }
}
