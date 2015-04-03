// @formatter:off
package com.everhomes.core;

public enum CoordinationLocks {
    CREATE_FAMILY("family.create");

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
        
        return null;
    }
}
