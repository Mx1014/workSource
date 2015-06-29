package com.everhomes.organization;


public enum OrganizationType {
    PM("PM"),GARC("GARC"),GANC("GANC"),GAPS("GAPS");
    
    private String code;
    private OrganizationType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static OrganizationType fromCode(String code) {
    	OrganizationType[] values = OrganizationType.values();
        for(OrganizationType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}