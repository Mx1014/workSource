package com.everhomes.rest.acl;

import com.everhomes.util.StringHelper;

public class ServiceModulePrivilegeDTO {
	private Long id;
	private Long moduleId;
	private Byte privilegeType;
	private Long privilegeId;
	private String remark;
	private Integer defaultOrder;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public Byte getPrivilegeType() {
		return privilegeType;
	}

	public void setPrivilegeType(Byte privilegeType) {
		this.privilegeType = privilegeType;
	}

	public Long getPrivilegeId() {
		return privilegeId;
	}

	public void setPrivilegeId(Long privilegeId) {
		this.privilegeId = privilegeId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getDefaultOrder() {
		return defaultOrder;
	}

	public void setDefaultOrder(Integer defaultOrder) {
		this.defaultOrder = defaultOrder;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
