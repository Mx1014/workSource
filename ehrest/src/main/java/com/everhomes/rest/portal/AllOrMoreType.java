// @formatter:off
package com.everhomes.rest.portal;

/**
 * <ul>全部更多类型
 * <li>ALL("all")：全部</li>
 * <li>MORE("more"): 更多</li>
 * </ul>
 */
public enum AllOrMoreType {

    ALL("all"),
    MORE("more");

    private String code;

    private AllOrMoreType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static AllOrMoreType fromCode(String code) {
        if(null != code){
            for (AllOrMoreType value: AllOrMoreType.values()) {
                if(value.code.equals(code)){
                    return value;
                }
            }
        }
        return null;
    }
}
