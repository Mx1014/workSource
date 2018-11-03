package com.everhomes.rest.pmtask;

/**
 * <ul>
 * <li>id: 主键</li>
 * <li>namespaceId: 域空间Id</li>
 * <li>ownerType: 项目类型</li>
 * <li>ownerId: 项目Id</li>
 * <li>taskCategoryId: 应用类型：6为物业报修（1为正中会报修），9为投诉建议</li>
 * </ul>
 */
public class GetPmTaskConfigCommand {

    private Long id;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private Long taskCategoryId;

    private Long appId;

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

    public Long getTaskCategoryId() {
        return taskCategoryId;
    }

    public void setTaskCategoryId(Long taskCategoryId) {
        this.taskCategoryId = taskCategoryId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
