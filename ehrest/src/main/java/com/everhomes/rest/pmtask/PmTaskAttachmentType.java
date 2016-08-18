// @formatter:off
package com.everhomes.rest.pmtask;

/**
 * <ul>
 * <li>IMAGE("image"): 图片</li>
 * </ul>
 */
public enum PmTaskAttachmentType {
    IMAGE("image");
    
    private String code;
    private PmTaskAttachmentType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PmTaskAttachmentType fromCode(String code) {
        if(code != null) {
            PmTaskAttachmentType[] values = PmTaskAttachmentType.values();
            for(PmTaskAttachmentType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
