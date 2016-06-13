// @formatter:off
package com.everhomes.rest.forum;

/**
 * <p>帖子创建者标签</p>
 * <ul>
 * <li>USER("USER"): 普通用户</li>
 * <li>BIZ("BIZ"): 商家</li>
 * <li>PM("PM"): 物业</li>
 * <li>GARC("GARC"): 业委，Government Agency - Resident Committee</li>
 * <li>GANC("GANC"): 居委，Government Agency - Neighbor Committee</li>
 * <li>GAPS("GAPS"): 公安，Government Agency - Police Station</li>
 * <li>GACW("GACW"): 社区工作站，Government Agency - Community Workstation</li>
 * </ul>
 */
public enum PostEntityTag {
    USER("USER"), BIZ("BIZ"), PM("PM"), GARC("GARC"), GANC("GANC"), GAPS("GAPS"), GACW("GACW");
    
    private String code;
    private PostEntityTag(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PostEntityTag fromCode(String code) {
        PostEntityTag[] values = PostEntityTag.values();
        for(PostEntityTag value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}
