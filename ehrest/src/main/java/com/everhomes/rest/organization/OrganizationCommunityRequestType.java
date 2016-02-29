package com.everhomes.rest.organization;

public enum OrganizationCommunityRequestType {
    Organization("organization");
    
    private String code;
    
    private OrganizationCommunityRequestType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static OrganizationCommunityRequestType fromCode(String code) {
        if(code == null) {
            return null;
        }
        
        if(code.equals("organization"))
                return Organization;
        
        return null;
    }
}
