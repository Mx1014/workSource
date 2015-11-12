// @formatter:off
package com.everhomes.user;

public enum UserCurrentEntityType {
    COMMUNITY("community"), FAMILY("family"), ORGANIZATION("organization");

    private String code;
    
    private UserCurrentEntityType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static UserCurrentEntityType fromCode(String code) {
        if(code != null) {
            for(UserCurrentEntityType value : UserCurrentEntityType.values()) {
                if(code.equalsIgnoreCase(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
    
    public String getUserProfileKey() {
        if(code == null) {
            return code;
        } else {
            return "user-current-" + code;
        }
    }
}
