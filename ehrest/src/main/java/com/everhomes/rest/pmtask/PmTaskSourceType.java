// @formatter:off
package com.everhomes.rest.pmtask;

/**
 * <ul>
 * <li></li>
 * </ul>
 */
public enum PmTaskSourceType {
    MOBILE("mobile"), EMAIL("email"), APP("app"), VISIT("visit"), MAILBOX("mailbox"), OTHER("other");
    
    private String code;
    private PmTaskSourceType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PmTaskSourceType fromCode(String code) {
        if(code != null) {
            PmTaskSourceType[] values = PmTaskSourceType.values();
            for(PmTaskSourceType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
