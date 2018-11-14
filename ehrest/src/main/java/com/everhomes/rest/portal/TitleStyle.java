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
    NONE(0), LEFT_ONE(101), LEFT_TWO(102), LEFT_THREE(103), LEFT_FOUR(104),
    CENTER_ONE(201), CENTER_TWO(202), CENTER_THREE(203);

    private int code;

    private TitleStyle(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static TitleStyle fromCode(Integer code) {
        if (null != code) {
            for (TitleStyle value : TitleStyle.values()) {
                if (value.code == code.intValue()) {
                    return value;
                }
            }
        }
        return null;
    }
}
