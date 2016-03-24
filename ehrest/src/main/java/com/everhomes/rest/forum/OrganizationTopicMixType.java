// @formatter:off
package com.everhomes.rest.forum;

/**
 * <p>帖子混合查询类型</p>
 * <ul>
 * <li>CHILDREN_ALL("children_all"): 包含自身及其所有子公司的帖子</li>
 * <li>COMMUNITY_ALL("community_all"): 包含自身所管辖的所有小区的帖子</li>
 * </ul>
 */
public enum OrganizationTopicMixType {
    CHILDREN_ALL("children_all"), COMMUNITY_ALL("community_all");
    
    private String code;
    private OrganizationTopicMixType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static OrganizationTopicMixType fromCode(String code) {
        if(code != null) {
            OrganizationTopicMixType[] values = OrganizationTopicMixType.values();
            for(OrganizationTopicMixType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
