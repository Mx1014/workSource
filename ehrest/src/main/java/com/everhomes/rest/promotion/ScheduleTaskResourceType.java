package com.everhomes.rest.promotion;

public enum ScheduleTaskResourceType {
    PROMOTION_ACTIVITY("activity");
    
    private String code;
    private ScheduleTaskResourceType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static ScheduleTaskResourceType fromCode(String code) {
        if(code == null) {
            return null;
        }
        
        if(code.equalsIgnoreCase(PROMOTION_ACTIVITY.getCode())) {
            return PROMOTION_ACTIVITY;
        }

        return null;
    }
}
