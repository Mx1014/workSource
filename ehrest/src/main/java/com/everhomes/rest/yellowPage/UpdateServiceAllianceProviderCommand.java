package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
* <ul>
* <li>id : 服务商id</li>
* <li>name: 服务商名称</li>
* <li>categoryId: 服务类型Id</li>
* <li>mail: 企业邮箱</li>
* <li>contactNumber: 号码</li>
* <li>contactName: 姓名</li>
* </ul>
*  @author
*  huangmingbo 2018年5月17日
**/

public class UpdateServiceAllianceProviderCommand {
	
	@NotNull
	private Long id;
	
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
	private Long appId;
	
	@NotNull
	private Long currentPMId;
	
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
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


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getAppId() {
		return appId;
	}


	public void setAppId(Long appId) {
		this.appId = appId;
	}


	public Long getCurrentPMId() {
		return currentPMId;
	}


	public void setCurrentPMId(Long currentPMId) {
		this.currentPMId = currentPMId;
	}

}
