package com.everhomes.rest.promotion;

public enum OpPromotionOwnerType {
    USER("user");
    
    private String code;
    private OpPromotionOwnerType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static OpPromotionOwnerType fromCode(String code) {
        if(code == null) {
            return null;
        }
        
        if(code.equalsIgnoreCase(USER.getCode())) {
            return USER;
        }

        return null;
    }
}
