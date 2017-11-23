// @formatter:off
package com.everhomes.rest.portal;

/**
 * <ul>item跳转类型
 * <li>NONE("None")：无</li>
 * <li>LAYOUT("Layout"): 门户</li>
 * <li>MODULEAPP("ModuleApp")：业务应用</li>
 * <li>ZUOLINURL("ZuoLinUrl")：左邻链接</li>
 * <li>NTHIRDURL("ThirdUrl")：第三方链接</li>
 * <li>ALLORMORE("AllOrMore");：全部或更多</li>
 * </ul>
 */
public enum PortalItemActionType {

    NONE("None"),
    LAYOUT("Layout"),
    MODULEAPP("ModuleApp"),
    ZUOLINURL("ZuoLinUrl"),
    THIRDURL("ThirdUrl"),
    ALLORMORE("AllOrMore");

    private String code;

    private PortalItemActionType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PortalItemActionType fromCode(String code) {
        if(null != code){
            for (PortalItemActionType value: PortalItemActionType.values()) {
                if(value.code.equals(code)){
                    return value;
                }
            }
        }
        return null;
    }
}
