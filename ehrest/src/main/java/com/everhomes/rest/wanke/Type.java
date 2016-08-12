package com.everhomes.rest.wanke;

/**
 * <ul>
 * <li>WANKE: 万科</li>
 * <li></li>
 * </ul>
 */
public enum Type {
	WANKE("wanke");
    
    private String code;
    private Type(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static Type fromCode(String code) {
        if(code != null) {
        	Type[] values = Type.values();
            for(Type value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
