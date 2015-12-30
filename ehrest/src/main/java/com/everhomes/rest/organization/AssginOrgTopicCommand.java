// @formatter:off
package com.everhomes.rest.organization;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId : 组织id</li>
 * <li>topicId: 帖子ID</li>
 * <li>userId: 接收任务的用户Id</li>
 * <li>contactPhone: 电话号码</li>
 * <li>namespaceId: namespaceId</li>
 * </ul>
 */
public class AssginOrgTopicCommand {
	
	@NotNull
	private Long parentId;
	@NotNull
	private Long organizationId;
	@NotNull
	private Long topicId;
	@NotNull
    private Long userId;
	
	private String contactPhone;
	
	private Integer namespaceId;
    
    public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public AssginOrgTopicCommand() {
    }

    public Long getTopicId() {
		return topicId;
	}
    

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	
	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	
	

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
