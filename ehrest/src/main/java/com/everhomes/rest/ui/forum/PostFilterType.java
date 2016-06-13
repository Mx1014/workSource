// @formatter:off
package com.everhomes.rest.ui.forum;

/**
 * <ul>
 * <li>DISCOVERY("discovery"): 用于发现界面的通用帖子查询列表</li>
 * <li>GA_NOTICE("ga_notice"): 政府相关的公告，用于物业、公安等界面的帖子查询列表</li>
 * </ul>
 */
public enum PostFilterType {
	DISCOVERY("discovery"), GA_NOTICE("ga_notice");
    
    private String code;
    private PostFilterType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }

	public static PostFilterType fromCode(String code) {
        PostFilterType[] values = PostFilterType.values();
        for(PostFilterType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }

        return null;
    }

}
