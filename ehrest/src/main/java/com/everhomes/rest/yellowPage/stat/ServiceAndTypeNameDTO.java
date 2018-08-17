package com.everhomes.rest.yellowPage.stat;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 服务id</li>
 * <li>name: 服务名称</li>
 * <li>serviceTypeId: 服务类型id</li>
 * <li>serviceTypeName: 服务类型名称</li>
 * </ul>
 */
public class ServiceAndTypeNameDTO {

	private Long id;
	private String name;
	private Long serviceTypeId;
	private String serviceTypeName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public String getServiceTypeName() {
		return serviceTypeName;
	}

	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
