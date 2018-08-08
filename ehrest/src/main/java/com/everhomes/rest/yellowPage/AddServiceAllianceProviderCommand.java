package com.everhomes.rest.yellowPage;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
* <ul>
* <li>namespaceId : 域空间id</li>
* <li>ownerType: 所属类型</li>
* <li>ownerId: 所属ID</li>
* <li>appId: 应用id</li>
* <li>type: 服务联盟类型值</li>
* <li>name: 服务商名称</li>
* <li>categoryId: 服务类型Id</li>
* <li>mail: 企业邮箱</li>
* <li>contactNumber: 号码</li>
* <li>contactName: 姓名</li>
* <li>currentPMId: 校验权限时填管理公司id</li>
* </ul>
*  @author
*  huangmingbo 2018年5月17日
**/

public class AddServiceAllianceProviderCommand {
	@NotNull
	private Integer namespaceId;
	
	@NotNull
	private String ownerType;
	
	@NotNull
	private Long ownerId;
	
	@NotNull
	private Long appId;
	
	@NotNull
	private Long type;
	
	@NotNull
	private String name;
	
	@NotNull
	private Long categoryId;
	
	@NotNull
	private String mail;
	
	@NotNull
	private String contactNumber;
	
	@NotNull
	private String contactName;
	
	@NotNull
	private Long currentPMId;
	

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Long getCurrentPMId() {
		return currentPMId;
	}

	public void setCurrentPMId(Long currentPMId) {
		this.currentPMId = currentPMId;
	}

}

