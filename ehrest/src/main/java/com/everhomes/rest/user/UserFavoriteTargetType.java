// @formatter:off
package com.everhomes.rest.user;

public enum UserFavoriteTargetType {
    TOPIC("topic"), BIZ("biz"), ACTIVITY("activity");
    
    private String code;
    
    private UserFavoriteTargetType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static UserFavoriteTargetType fromCode(String code) {
        if(code != null) {
            UserFavoriteTargetType[] values = UserFavoriteTargetType.values();
            for(UserFavoriteTargetType value : values) {
                if(code.equals(value.getCode())) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
