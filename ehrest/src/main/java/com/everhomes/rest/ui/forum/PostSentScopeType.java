// @formatter:off
package com.everhomes.rest.ui.forum;

/**
 * <ul>
 * <li>DISCOVERY("discovery"): 用于发现界面的通用帖子发送范围列表</li>
 * <li>GA("ga"): 政府相关，用于物业、公安等界面的帖子发送范围列表</li>
 * </ul>
 */
public enum PostSentScopeType {
	DISCOVERY("discovery"), GA("ga");
    
    private String code;
    private PostSentScopeType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }

	public static PostSentScopeType fromCode(String code) {
        PostSentScopeType[] values = PostSentScopeType.values();
        for(PostSentScopeType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }

        return null;
    }

}
