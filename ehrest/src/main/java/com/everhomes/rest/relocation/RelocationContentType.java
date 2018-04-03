// @formatter:off
package com.everhomes.rest.relocation;

/**
 * <ul>
 * <li>IMAGE("image"): 图片</li>
 * </ul>
 */
public enum RelocationContentType {
	IMAGE("image");

    private String code;
    private RelocationContentType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static RelocationContentType fromCode(String code) {
        if(code != null) {
            RelocationContentType[] values = RelocationContentType.values();
            for(RelocationContentType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
