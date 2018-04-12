package com.everhomes.rest.community;

/**
 * <ul>
 *     <li>ONLY_COMMUNITY: 只选取园区</li>
 *     <li>CATEGORY_COMMUNITY: 选取项目分类+园区</li>
 *     <li>CATEGORY_COMMUNITY_CHILDPROJECT: 选取项目分类+园区+子项目</li>
 *     <li>All: 选取项目分类+园区+子项目+楼栋（全部）</li>
 * </ul>
 */
public enum  CommunityFetchType {
    ONLY_COMMUNITY("ONLY_COMMUNITY"), CATEGORY_COMMUNITY("CATEGORY_COMMUNITY"), CATEGORY_COMMUNITY_CHILDPROJECT("CATEGORY_COMMUNITY_CHILDPROJECT"), ALL("ALL");

    private String code;
    private CommunityFetchType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static CommunityFetchType fromCode(String code) {
        CommunityFetchType[] values = CommunityFetchType.values();
        for(CommunityFetchType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }

        return null;
    }
}
