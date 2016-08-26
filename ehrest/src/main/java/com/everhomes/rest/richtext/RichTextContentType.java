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
    	if(code == null) {
    		return null;
    	}
        
        if(code.equalsIgnoreCase(RICHTEXT.getCode())) {
        	return RICHTEXT;
        }

        if(code.equalsIgnoreCase(LINK.getCode())) {
        	return LINK;
        }
        
        return null;
    }
}
