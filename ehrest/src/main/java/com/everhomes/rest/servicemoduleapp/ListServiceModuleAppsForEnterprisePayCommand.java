package com.everhomes.rest.servicemoduleapp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>enableEnterprisePay: 支持企业支付标记，0-否，1-是。参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class ListServiceModuleAppsForEnterprisePayCommand {
	private Integer namespaceId;

	private Byte enableEnterprisePay;


	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}


	public Byte getEnableEnterprisePay() {
		return enableEnterprisePay;
	}

	public void setEnableEnterprisePay(Byte enableEnterprisePay) {
		this.enableEnterprisePay = enableEnterprisePay;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
