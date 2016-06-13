// @formatter:off
package com.everhomes.rest.messaging;

public enum ChannelType {
    USER("user"), GROUP("group");
    
    private String code;
    private ChannelType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static ChannelType fromCode(String code) {
        if(code == null)
            return null;
        
        if(code.equalsIgnoreCase("user"))
            return USER;
        else if(code.equalsIgnoreCase("group"))
            return GROUP;
        
        return null;
    }
}
