// @formatter:off
package com.everhomes.rest.promotion;

/**
 * <ul>
 *     <li>TEXT(1): 文本类型</li>
 *     <li>ICON(2): icon类型</li>
 *     <li>COMBINATION(3): 组合类型</li>
 * </ul>
 */
public enum ModulePromotionInfoType {

    TEXT((byte)1), ICON((byte)2), COMBINATION((byte)3);

    private Byte code;

    ModulePromotionInfoType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static ModulePromotionInfoType fromCode(Byte code) {
        if (code != null) {
            for (ModulePromotionInfoType type : ModulePromotionInfoType.values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
        }
        return null;
    }
}
