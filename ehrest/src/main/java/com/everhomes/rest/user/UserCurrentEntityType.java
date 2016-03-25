// @formatter:off
package com.everhomes.rest.user;

/**
 * <ul>用户当前切换的实体
 * <li>COMMUNITY_RESIDENTIAL(community_residential): 住宅类型小区</li>
 * <li>COMMUNITY_COMMERCIAL(community_commercial): 商用类型园区</li>
 * <li>COMMUNITY(community): 小区/园区，在使用场景之后合起来客户端操作更方便</li>
 * <li>FAMILY(family): 家庭</li>
 * <li>ORGANIZATION(organization): 机构，在3.3版本以后机构已经与企业合并，不使用ENTERPRISE而使用ORGANIZATION</li>
 * <li>ENTERPRISE(enterprise): 企业，在3.3版本以后机构已经与企业合并，不使用ENTERPRISE而使用ORGANIZATION</li>
 * </ul>
 */
public enum UserCurrentEntityType {
    COMMUNITY_RESIDENTIAL("community_residential"), 
    COMMUNITY_COMMERCIAL("community_commercial"),
    ENTERPRISE("enterprise"), 
    
    
    /*以后要用的实体*/
    FAMILY("family"), 
    ORGANIZATION("organization"),
    COMMUNITY("community");

    private String code;
    
    private UserCurrentEntityType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static UserCurrentEntityType fromCode(String code) {
        if(code != null) {
            for(UserCurrentEntityType value : UserCurrentEntityType.values()) {
                if(code.equalsIgnoreCase(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
    
    public String getUserProfileKey() {
        if(code == null) {
            return code;
        } else {
            return "user-current-" + code;
        }
    }
}
