// @formatter:off
package com.everhomes.rest.common;


/**
 * <p>实体类型:</p>
 * <ul>
 * <li>USER：用户</li>
 * <li>GROUP: 组</li>
 * <li>FORUM: 论坛</li>
 * <li>ADDRESS: 地址</li>
 * <li>CATEGORY: 类型</li>
 * <li>COMMUNITY: 小区</li>
 * <li>REGION: 活动</li>
 * <li>POST: 帖子/评论</li>
 * <li>FAMILY: 家庭</li>
 * <li>ORGANIZATIONS: 机构</li>
 * <li>BUILDING: 楼栋</li>
 * </ul>
 */
public enum EntityType {
    USER("EhUsers"),
    GROUP("EhGroups"),
    FORUM("EhForums"),
    ADDRESS("EhForums"),
    CATEGORY("EhCategories"),
    COMMUNITY("EhCommunities"),
    POST("EhForumPosts"),
    ACTIVITY("EhActivities"),
    FAMILY("EhFamilies"),
    TOPIC("EhTopics"),
    ORGANIZATIONS("EhOrganizations"),
    BUILDING("EhBuildings");

    private String code;
    
    private EntityType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static EntityType fromCode(String code) {
        if(code == null)
            return null;
        
        if(code.equalsIgnoreCase(USER.getCode()))
            return USER;
        else if(code.equalsIgnoreCase(GROUP.getCode()))
            return GROUP;
        else if(code.equalsIgnoreCase(FORUM.getCode()))
            return FORUM;
        else if(code.equalsIgnoreCase(ADDRESS.getCode()))
            return ADDRESS;
        else if(code.equalsIgnoreCase(CATEGORY.getCode()))
            return CATEGORY;
        else if(code.equalsIgnoreCase(COMMUNITY.getCode()))
            return COMMUNITY;
        else if(code.equalsIgnoreCase(FAMILY.getCode()))
            return FAMILY;
        else if(code.equalsIgnoreCase(POST.getCode()))
            return POST;
        else if(code.equalsIgnoreCase(ORGANIZATIONS.getCode()))
            return ORGANIZATIONS;
        else if(code.equalsIgnoreCase("EhFamilies"))
            return FAMILY;
        else if(code.equalsIgnoreCase("EhTopics"))
            return TOPIC;
        else if(code.equalsIgnoreCase("EhBuildings"))
        	return BUILDING;
        for (EntityType entityType : EntityType.values()) {
            if (entityType.getCode().equals(code)) {
                return entityType;
            }
        }
        return null;
    }
}
