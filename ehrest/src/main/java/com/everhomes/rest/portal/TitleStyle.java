// @formatter:off
package com.everhomes.rest.portal;

/**
 * <ul>
 *     <li>NONE((byte) 0): 无标题</li>
 *     <li>LEFT_ONE((byte) 101): 左1</li>
 *     <li>LEFT_TWO((byte) 102): 左2</li>
 *     <li>LEFT_THREE((byte) 103): 左3</li>
 *     <li>LEFT_FOUR((byte) 104): 左4</li>
 *     <li>CENTER_ONE((byte) 201): 中1</li>
 *     <li>CENTER_TWO((byte) 202): 中2</li>
 *     <li>CENTER_THREE((byte) 203): 中3</li>
 * </ul>
 */
public enum TitleStyle {
    NONE((byte) 0), LEFT_ONE((byte) 101), LEFT_TWO((byte) 102), LEFT_THREE((byte) 103), LEFT_FOUR((byte) 104),
    CENTER_ONE((byte) 201), CENTER_TWO((byte) 202), CENTER_THREE((byte) 203);

    private byte code;

    private TitleStyle(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static TitleStyle fromCode(Byte code) {
        if (null != code) {
            for (TitleStyle value : TitleStyle.values()) {
                if (value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        return null;
    }
}
