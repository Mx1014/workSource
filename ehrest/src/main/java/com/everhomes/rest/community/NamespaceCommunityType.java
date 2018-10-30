package com.everhomes.rest.community;

/**
 * <ul>来自于第三方楼栋的类型，对应eh_communities表的 namespace_community_type字段
 * <li>SHENZHOU("shenzhou"): 神州数码</li>
 * <li>EBEI("ebei"): 一碑</li>
 * <li>RUIAN_CM("ruian_cm"): 瑞安</li>
 * </ul>
 * Created by ying.xiong on 2017/8/9.
 */
public enum NamespaceCommunityType {
    SHENZHOU("shenzhou"), RUIAN_CM("ruian_cm"), EBEI("ebei");

    private String code;
    private NamespaceCommunityType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static NamespaceCommunityType fromCode(String code) {
        if(code != null) {
            NamespaceCommunityType[] values = NamespaceCommunityType.values();
            for(NamespaceCommunityType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }

        return null;
    }
}
