package com.everhomes.rest.richtext;


/**
 * <ul>
 * <li>ABOUT: 关于</li>
 * <li>INTRODUCTION: 简介</li>
 * <li>AGREEMENT: 协议</li>
 * </ul>
 */
public enum RichTextResourceType {

	ABOUT("about"), INTRODUCTION("introduction"), AGREEMENT("agreement");
	
	private String code;
    private RichTextResourceType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static RichTextResourceType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
        
        if(code.equalsIgnoreCase(ABOUT.getCode())) {
        	return ABOUT;
        }

        if(code.equalsIgnoreCase(INTRODUCTION.getCode())) {
        	return INTRODUCTION;
        }
        
        if(code.equalsIgnoreCase(AGREEMENT.getCode())) {
        	return AGREEMENT;
        }

        return null;
    }
}
