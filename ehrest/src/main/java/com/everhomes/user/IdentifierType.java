// @formatter:off
package com.everhomes.user;

public enum IdentifierType {
    mobile, email;
    
    public static IdentifierType fromString(String val) {
        if(val.equalsIgnoreCase("mobile"))
            return mobile;
        else if(val.equalsIgnoreCase("email"))
            return email;
        
        throw new RuntimeException("Invalid IdentifierType " + val);
    }
}
