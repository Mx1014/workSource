package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

/**
* <ul>
* <li>id : id</li>
* <li>name: 服务商名称</li>
* <li>categoryId: 服务类型Id</li>
* <li>categoryName: 服务类型名称</li>
* <li>mail: 企业邮箱</li>
* <li>contactNumber: 号码</li>
* <li>contactName: 姓名</li>
* <li>score: 评分结果</li>
* </ul>
*  @author
*  huangmingbo 2018年5月17日
**/

public class ServiceAllianceProviderDTO {
	private Long id;
	private String name;
	private Long categoryId;
	private String categoryName;
	private String mail;
	private String contactNumber;
	private String contactName;
	private Double score;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

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

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}
