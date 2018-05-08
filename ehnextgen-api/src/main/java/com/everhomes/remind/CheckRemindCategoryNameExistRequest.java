package com.everhomes.remind;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 当前分类的ID</li>
 * <li>namespaceId: 域空间ID</li>
 * <li>ownerType: 默认EhOrganizations</li>
 * <li>ownerId: 总公司ID</li>
 * <li>userId: 分类所有人的uid</li>
 * <li>name: 分类的名称</li>
 * </ul>
 */
public class CheckRemindCategoryNameExistRequest {
    private Long id;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private Long userId;
    private String name;

    public CheckRemindCategoryNameExistRequest() {

    }

    public CheckRemindCategoryNameExistRequest(Long id, Integer namespaceId, String ownerType, Long ownerId, Long userId, String name) {
        this.id = id;
        this.namespaceId = namespaceId;
        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.userId = userId;
        this.name = name;
    }

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
