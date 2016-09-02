// @formatter:off
package com.everhomes.rest.pmtask;

/**
 * <ul>
 * <li>IMAGE("image"): 图片</li>
 * </ul>
 */
public enum PmTaskContentType {
	IMAGE("image");
    
    private String code;
    private PmTaskContentType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PmTaskContentType fromCode(String code) {
        if(code != null) {
            PmTaskContentType[] values = PmTaskContentType.values();
            for(PmTaskContentType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
