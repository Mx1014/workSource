// @formatter:off
package com.everhomes.rest.ui.user;

/**
 * <ul>场景
 * <li>DEFAULT(default)：默认，普通用户场景</li>
 * <li>PM_ADMIN(pm_admin): 物业管理员场景（左邻版是小区物业管理员，园区版则是园区管理员）</li>
 * </ul>
 */
public enum SceneType {
    DEFAULT("default"), PM_ADMIN("pm_admin");
    
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
