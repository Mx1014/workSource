package com.everhomes.rest.servicemoduleapp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>enableEnterprisePayFlag: 支持企业支付标记，0-否，1-是。参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class ListServiceModuleAppsForEnterprisePayCommand {
	private Integer namespaceId;

	private Byte enableEnterprisePayFlag;


	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Byte getEnableEnterprisePayFlag() {
		return enableEnterprisePayFlag;
	}

	public void setEnableEnterprisePayFlag(Byte enableEnterprisePayFlag) {
		this.enableEnterprisePayFlag = enableEnterprisePayFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
