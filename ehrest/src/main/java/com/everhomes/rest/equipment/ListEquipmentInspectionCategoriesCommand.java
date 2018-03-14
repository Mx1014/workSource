package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>ownerId: 拥有者id（机构id）</li>
 *  <li>namespaceId: namespaceId</li>
 * </ul>
 */
public class ListEquipmentInspectionCategoriesCommand {

	private Long ownerId;

	private Integer namespaceId;

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
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
