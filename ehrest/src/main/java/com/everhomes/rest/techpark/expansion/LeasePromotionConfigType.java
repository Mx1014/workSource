// @formatter:off
package com.everhomes.rest.techpark.expansion;

public enum LeasePromotionConfigType {
    RENT_AMOUNT_FLAG("rentAmountFlag"),
    RENT_AMOUNT_UNIT("rentAmountUnit"),
    ISSUING_LEASE_FLAG("issuingLeaseFlag"),
    RENEW_FLAG("renewFlag"),
    AREA_SEARCH_FLAG("areaSearchFlag"),
    CONSULT_FLAG("consultFlag"),
    BUILDING_INTRODUCE_FLAG("buildingIntroduceFlag"),
    DISPLAY_NAME_STR("displayNameStr"),
    DISPLAY_ORDER_STR("displayOrderStr");

    private String code;
    private LeasePromotionConfigType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static LeasePromotionConfigType fromCode(String code) {
        if(code != null) {
            LeasePromotionConfigType[] values = LeasePromotionConfigType.values();
            for(LeasePromotionConfigType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
