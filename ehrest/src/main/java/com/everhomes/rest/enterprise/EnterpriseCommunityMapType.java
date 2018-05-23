package com.everhomes.rest.enterprise;

public enum EnterpriseCommunityMapType {
    Enterprise("enterprise"),
    Organization("organization");
    
    private String code;
    
    private EnterpriseCommunityMapType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static EnterpriseCommunityMapType fromCode(String code) {
        if(code == null) {
            return null;
        }
        
        if(code.equals("enterprise"))
                return Enterprise;
        
        return null;
    }
}
