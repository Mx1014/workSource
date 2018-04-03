package com.everhomes.rest.richtext;

/**
 * <ul>
 * <li>RICHTEXT: 富文本</li>
 * <li>LINK: url链接</li>
 * </ul>
 */
public enum RichTextContentType {

	RICHTEXT("richtext"), LINK("link");
	
	private String code;
    private RichTextContentType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static RichTextContentType fromCode(String code) {
        if(code != null) {
            for(RichTextContentType type : RichTextContentType.values()) {
                if(type.code.equalsIgnoreCase(code)) {
                    return type;
                }
            }
        }

        return null;
    }
}
