// @formatter:off
package com.everhomes.rest.pmtask;

public enum PmTaskAttachmentType {
    TASK("task"), TASKLOG("tasklog");
    
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
