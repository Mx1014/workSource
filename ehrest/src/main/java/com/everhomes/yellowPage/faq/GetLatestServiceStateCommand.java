package com.everhomes.yellowPage.faq;

import com.everhomes.rest.yellowPage.AllianceCommonCommand;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: namespaceId</li>
 * <li>ownerType: ownerType</li>
 * <li>ownerId: ownerId</li>
 * <li>type: 服务联盟类型</li>
 * </ul>
 **/
public class GetLatestServiceStateCommand {
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	private Long type;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
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

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}
}
