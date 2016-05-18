// @formatter:off
package com.everhomes.rest.ui.user;

/**
 * <ul>场景
 * <li>DEFAULT(default)：默认，普通用户场景，小区版游客，为了兼容以前版本，名称不修改（实体对象对应{@link com.everhomes.rest.address.CommunityDTO}</li>
 * <li>PM_ADMIN(pm_admin): 物业管理员场景（左邻版是小区物业管理员，园区版则是园区管理员），小区版本，为了兼容以前版本，名称不修改
 *    （实体对象对应{@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * <li>FAMILY(family): 家庭场景（实体对象对应{@link  com.everhomes.rest.family.FamilyDTO}</li>
 * <li>ENTERPRISE(enterprise): 普通公司场景（不分左邻版或园区版），在公司已经认证（实体对象对应{@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * <li>ENTERPRISE_NOAUTH(enterprise_noauth): 普通公司场景（不分左邻版或园区版），用户没在公司认证（实体对象对应{@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * <li>PARK_TOURIST(park_tourist): 园区游客场景，未加入公司（实体对象对应{@link com.everhomes.rest.address.CommunityDTO}</li>
 * </ul>
 */
public enum SceneType {
    DEFAULT("default"), 
    PM_ADMIN("pm_admin"), 
    FAMILY("family"), 
    ENTERPRISE("enterprise"),
    ENTERPRISE_NOAUTH("enterprise_noauth"),
    PARK_TOURIST("park_tourist");
    
    private String code;
       
    private SceneType(String code) {
        this.code = code;
    }
       
    public String getCode() {
        return this.code;
    }
       
    public static SceneType fromCode(String code) {
        SceneType[] values = SceneType.values();
        for(SceneType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
       
        return null;
    }
}
