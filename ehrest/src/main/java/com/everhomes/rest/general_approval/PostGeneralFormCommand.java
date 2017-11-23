package com.everhomes.rest.general_approval;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul> 提交表单信息
 * <li>namespaceId：域空间id</li>
 * <li>ownerType：ownerType</li>
 * <li>ownerId：ownerId</li>
 * <li>currentOrganizationId：用户当前公司id</li>
 * <li>values: 审批项中，每项对应的值{@link PostApprovalFormItem} </li>
 * </ul>
 * @author janson
 *
 */
public class PostGeneralFormCommand {
	private Integer namespaceId;
	private Long ownerId;

	private String ownerType;

	private String sourceType;
	private Long sourceId;

    private Long currentOrganizationId;

	@ItemType(PostApprovalFormItem.class)
	private List<PostApprovalFormItem> values;

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public List<PostApprovalFormItem> getValues() {
		return values;
	}

	public void setValues(List<PostApprovalFormItem> values) {
		this.values = values;
	}

    public Long getCurrentOrganizationId() {
        return currentOrganizationId;
    }

    public void setCurrentOrganizationId(Long currentOrganizationId) {
        this.currentOrganizationId = currentOrganizationId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
