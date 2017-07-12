package com.everhomes.rest.widget;


/**
 * <ul>
 * <li>PURE_TEXT: 1，纯文本</li>
 * <li>TEXT_WITH_BOARD: 2，带边框的文本</li>
 * <li>TEXT_WITH_ICON: 3，icon型（小图）</li> 
 * <li>TEXT_WITH_IMAGE: 4，image型（大图）</li> 
 * </ul>
 */
public enum AssociactionCategoryStyle {

	PURE_TEXT((byte)1), TEXT_WITH_BOARD((byte)2), TEXT_WITH_ICON((byte)3), TEXT_WITH_IMAGE((byte)4); 
	
	private byte code;
    private AssociactionCategoryStyle(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static AssociactionCategoryStyle fromCode(Byte code) {
    	if (code != null) {
			for (AssociactionCategoryStyle style : AssociactionCategoryStyle.values()) {
				if (style.getCode() == code.byteValue()) {
					return style;
				}
			}
		}
    	return null;
    }
    
    public static AssociactionCategoryStyle fromCode(String code) {
    	if (code != null) {
			for (AssociactionCategoryStyle style : AssociactionCategoryStyle.values()) {
				if (code.equals(String.valueOf(style.getCode()))) {
					return style;
				}
			}
		}
    	return null;
    }
}
