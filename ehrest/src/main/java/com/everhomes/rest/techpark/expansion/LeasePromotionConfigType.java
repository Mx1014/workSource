// @formatter:off
package com.everhomes.rest.techpark.expansion;

/**
 * <ul>
 * <li>rentAmountFlag: 租金启用标志</li>
 * <li>rentAmountUnit: 租金单位</li>
 * <li>issuingLeaseFlag: 用户是否可发布招租信息</li>
 * <li>renewFlag: 续租是否显示</li>
 * <li>areaSearchFlag: 是否支持面积搜索</li>
 * <li>consultFlag: 是否显示电话咨询按钮</li>
 * <li>buildingIntroduceFlag: 楼栋介绍是否显示</li>
 * <li>displayNamesV2: 显示名称字符串</li>
 * <li>displayOrdersV2: 显示排序，与名称对应</li>
 * </ul>
 */
public enum LeasePromotionConfigType {
    RENT_AMOUNT_FLAG("rentAmountFlag"),
    RENT_AMOUNT_UNIT("rentAmountUnit"),
    ISSUING_LEASE_FLAG("issuingLeaseFlag"),
    RENEW_FLAG("renewFlag"),
    AREA_SEARCH_FLAG("areaSearchFlag"),
    CONSULT_FLAG("consultFlag"),
    BUILDING_INTRODUCE_FLAG("buildingIntroduceFlag"),
    DISPLAY_NAME_STR("displayNameStr"),
    DISPLAY_ORDER_STR("displayOrderStr"),
    HIDE_ADDRESS_FLAG("hideAddressFlag");

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
