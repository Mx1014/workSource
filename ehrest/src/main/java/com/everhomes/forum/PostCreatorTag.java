// @formatter:off
package com.everhomes.forum;

/**
 * <p>帖子创建者标签</p>
 * <ul>
 * <li>USER: 普通用户</li>
 * <li>PM: 物业</li>
 * <li>BIZ: 商家</li>
 * </ul>
 */
public enum PostCreatorTag {
    USER("/user"), PM("/pm"), BIZ("/biz");
    
    private String code;
    private PostCreatorTag(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PostCreatorTag fromCode(String code) {
        if(code == null)
            return null;
        
        if(code.equalsIgnoreCase(USER.getCode())) {
            return USER;
        }
        if(code.equalsIgnoreCase(PM.getCode())) {
            return PM;
        }
        if(code.equalsIgnoreCase(BIZ.getCode())) {
            return BIZ;
        }
        
        return null;
    }
}
