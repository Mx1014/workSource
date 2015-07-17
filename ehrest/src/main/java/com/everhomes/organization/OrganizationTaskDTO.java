// @formatter:off
package com.everhomes.organization;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 任务ID</li>
 * <li>organizationId: 机构id </li>
 * <li>entityType: 承载任务的实体类型，参考{@link com.everhomes.organization.OrganizationTaskApplyEnityType}</li>
 * <li>entityId: 承载任务的实体类型对应的ID，如帖子id</li>
 * <li>targetType: 任务被指派的目标类型，参考{@link com.everhomes.organization.OrganizationTaskTargetType}</li>
 * <li>targetId: 任务被指派的目标类型对应的ID，如用户ID</li>
 * <li>taskType: 任务所属类型，参考{@link com.everhomes.organization.OrganizationTaskType}</li>
 * <li>description: 任务描述</li>
 * <li>taskStatus: 任务状态，参考{@link com.everhomes.organization.OrganizationTaskStatus}</li>
 * </ul>
 */
public class OrganizationTaskDTO {
    private Long id;
    private Long organizationId;
    private String organizationType;
    private String entityType;
    private Long entityId;
    private String targetType;
    private Long targetId;
    private String taskType;
    private String description;
    private Byte taskStatus;
    private Timestamp createTime;
    
	public OrganizationTaskDTO() {
    }

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Byte getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Byte taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
