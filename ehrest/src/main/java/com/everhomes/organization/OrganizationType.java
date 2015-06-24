package com.everhomes.organization;
public enum OrganizationType {
    PM("pm"),GARC("garc"),GANC("ganc"),GAPS("gaps");
    
    private String code;
    private OrganizationType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static OrganizationType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
        
        if(code.equalsIgnoreCase(PM.getCode())) {
        	return PM;
        }
        
        if(code.equalsIgnoreCase(GARC.getCode())) {
        	return GARC;
        }


        if(code.equalsIgnoreCase(GANC.getCode())) {
        	return GANC;
        }

        if(code.equalsIgnoreCase(GAPS.getCode())) {
        	return GAPS;
        }
        
       
        return null;
    }
}