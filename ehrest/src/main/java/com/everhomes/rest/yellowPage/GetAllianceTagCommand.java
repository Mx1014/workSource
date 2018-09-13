package com.everhomes.rest.yellowPage;



import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 参数
 * <li>namespaceId: 域空间ID</li>
 * <li>type: 服务联盟根类型</li>
 * <li>pageSize: 获取的数据条数，如需所有传空</li>
 * <li>pageAnchor: 锚点</li>
 * </ul>
 */
public class GetAllianceTagCommand {
	
	@NotNull
	private Integer namespaceId;
	
	@NotNull
	private String ownerType;
	
	@NotNull
	private Long ownerId;
	
	@NotNull
	private Long type;

	private Integer pageSize;
	private Long pageAnchor;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
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

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
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

}
