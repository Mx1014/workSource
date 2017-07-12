// @formatter:off
package com.everhomes.rest.techpark.expansion;

public enum LeaseIssuerType {
    NORMAL_USER("NORMAL_USER"), ORGANIZATION("ORGANIZATION");
    
    private String code;
    private LeaseIssuerType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static LeaseIssuerType fromCode(String code) {
        if(code != null) {
            LeaseIssuerType[] values = LeaseIssuerType.values();
            for(LeaseIssuerType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
