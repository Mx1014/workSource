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
 * <li>EhServiceModules: 业务模块</li>
 * <li>EhZuolinAdmins: 左邻管理</li>
 * </ul>
 */
public enum EntityType {
    NAMESPACE("EhNamespaces"),
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
    BUILDING("EhBuildings"),
    SERVICE_MODULE("EhServiceModules"),
    ROLE("EhAclRoles"),
    RESOURCE_CATEGORY("EhResourceCategories"),
    ZUOLIN_ADMIN("EhZuolinAdmins"),
    ALL("EhAll"),
    CHILD_PROJECT("child_project"),
    SERVICE_ALLIANCES("EhServiceAlliances"),
    ;

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
        for (EntityType entityType : EntityType.values()) {
            if (entityType.getCode().equals(code)) {
                return entityType;
            }
        }
        return null;
    }
}
