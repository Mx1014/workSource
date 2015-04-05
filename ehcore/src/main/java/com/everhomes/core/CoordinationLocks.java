// @formatter:off
package com.everhomes.core;

public enum CoordinationLocks {
    CREATE_FAMILY("family.create"),
    CREATE_ADDRESS("address.create"),
    CREATE_ADDRESS_STATS("address.stats.create");

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
        else if(code.equals("address.create"))
            return CREATE_ADDRESS;
        else if(code.equals("address.stats.create"))
            return CREATE_ADDRESS_STATS;
        
        return null;
    }
}
