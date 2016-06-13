// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>对于物业、业委、居委、公安、社区工作站相关的帖，有两种不同角度的查询：一是普通用户查询自己小区范围内有关物业/业委/居委/公安/社区工作站等的帖子；
 *    二是物业/业委/居委/公安/社区工作站等以管理员的身份查询自己所有能够管理的帖子；
 * <li>由于这些帖都是放在社区论坛，故只从社区论坛里查询，即论坛ID恒填ForumConstants.SYSTEM_FORUM(1)；</li>   
 * <li>对于第一种情况：entityTag填URSER，entityId填用户当前所在的小区ID，targetTag则填所要查询的实体类型（从物业、业委、居委、公安、社区工作站等中选一种）；
 *                   contentCategory选报修/咨询与求助/投诉与建议，actionCategory不填；</li>   
 * <li>对于第二种情况：entityTag填物业、业委、居委、公安、社区工作站等的一种、entityId填其对应的机构ID，targetTag可不填，若填的话则公告类型填USER，报修/咨询与求助/投诉与建议则填物业、业委、居委、公安、社区工作站等的一种；</li>   
 * </ul>
 * <ul>字段参数：
 * <li>forumId: 论坛ID，只支持社区论坛</li>
 * <li>entityTag: 查询者的实体标签，与帖子里的creatorTag和targetTag匹配，参考{@link com.everhomes.rest.forum.PostEntityTag}</li>
 * <li>entityId: 查询者实体使用的ID，可能为小区ID、也可能是机构ID</li>
 * <li>targetTag: 要查询的实体标签，与帖子里的creatorTag和targetTag匹配，参考{@link com.everhomes.rest.forum.PostEntityTag}</li>
 * <li>communityId: 用户当前所在小区ID，由客户传过来并在帖子结果中返回去，服务器端不使用、也不做逻辑；</li>
 * <li>contentCategory: 内容类型</li>
 * <li>actionCategory: 动作类型，对应以前的serviceType</li>
 * <li>pageAnchor: 开始的锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class QueryTopicByEntityAndCategoryCommand {
    private Long forumId;
    private String entityTag;
    private Long entityId;
    private String targetTag;
    private Long communityId;
    private Long contentCategory;
    private Long actionCategory;
    private Long pageAnchor;
    private Integer pageSize;
    
    public QueryTopicByEntityAndCategoryCommand() {
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public String getEntityTag() {
        return entityTag;
    }

    public void setEntityTag(String entityTag) {
        this.entityTag = entityTag;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getTargetTag() {
        return targetTag;
    }

    public void setTargetTag(String targetTag) {
        this.targetTag = targetTag;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getContentCategory() {
        return contentCategory;
    }

    public void setContentCategory(Long contentCategory) {
        this.contentCategory = contentCategory;
    }

    public Long getActionCategory() {
        return actionCategory;
    }

    public void setActionCategory(Long actionCategory) {
        this.actionCategory = actionCategory;
    }
	
    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
