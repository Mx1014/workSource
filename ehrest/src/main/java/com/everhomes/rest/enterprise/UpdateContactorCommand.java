package com.everhomes.rest.enterprise;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *  <li>contactName: 企业联系人姓名</li>
 *  <li>entryValue: 企业联系人手机号</li>
 *  <li>enterpriseId: 企业id</li>
 *  <li>communityId: 园区id</li>
 *  <li>namespaceId: 指定园区的标示</li>
 * </ul>
 *
 */
public class UpdateContactorCommand {
	
	private String contactName;
	
	@NotNull
	private String entryValue;
	@NotNull
	private Long enterpriseId;
	@NotNull
	private Long communityId;
	@NotNull
	private Integer namespaceId;

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getEntryValue() {
		return entryValue;
	}

	public void setEntryValue(String entryValue) {
		this.entryValue = entryValue;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	

}
