// @formatter:off
package com.everhomes.core;

public enum CoordinationLocks {
    CREATE_FAMILY("family.create"),
    LEAVE_FAMILY("family.leave"),
    CREATE_ADDRESS("address.create");

    private String code;
    private CoordinationLocks(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static CoordinationLocks fromCode(String code) {
        if(code == null)
            return null;
        
        if(code.equals("family.create"))
            return CREATE_FAMILY;
        else if(code.equals("family.leave"))
            return LEAVE_FAMILY;
        else if(code.equals("address.create"))
            return CREATE_ADDRESS;
        
        return null;
    }
}
