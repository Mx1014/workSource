// @formatter:off
package com.everhomes.rest.reserve;

/**
 * <p>归属类型</p>
 * <ul>
 * <li>DEFAULT("default"): 默认</li>
 * <li>RESOURCE("resource"): 某一个资源的使用规则</li>
 * </ul>
 */
public enum ReserveRuleOwnerType {
    DEFAULT("default"), RESOURCE("resource");

    private String code;
    private ReserveRuleOwnerType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static ReserveRuleOwnerType fromCode(String code) {
        if(code != null) {
            ReserveRuleOwnerType[] values = ReserveRuleOwnerType.values();
            for(ReserveRuleOwnerType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
