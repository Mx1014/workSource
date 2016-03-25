// @formatter:off
package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * 用户当前切换的实体，有可能是小区、家庭、机构等
 * <ul>
 * <li>entityType: 实体类型，参考{@link  com.everhomes.rest.user.UserCurrentEntityType}</li>
 * <li>entityId: 实体对应的ID</li>
 * <li>timestamp: 切换的时间</li>
 * <li>directlyEnterpriseId:群组的所属公司</li>
 * </ul>
 */
public class UserCurrentEntity {
    private Integer namespaceId;
    
    private String entityType;
    
    private String entityId;
    
    private String entityName;
    
    private Long timestamp;
    
    private Long directlyEnterpriseId;
    
    public UserCurrentEntity() {
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getDirectlyEnterpriseId() {
		return directlyEnterpriseId;
	}

	public void setDirectlyEnterpriseId(Long directlyEnterpriseId) {
		this.directlyEnterpriseId = directlyEnterpriseId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
