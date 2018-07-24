package com.everhomes.rest.policy;


import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>ownerType: 所属类型</li>
 * <li>ownerId: 所属项目id</li>
 * <li>categoryIds: 企业类型</li>
 * <li>title: 政策标题</li>
 * <li>outline: 政策概要</li>
 * <li>content: 政策详情</li>
 * <li>priority: 排序</li>
 * </ul>
 */
public class PolicyDTO {

    private Long id;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private List<Long> categoryIds;
    private String title;
    private String outline;
    private String content;
    private Long priority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
