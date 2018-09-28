package com.everhomes.rest.community.admin;


/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>keywords: 姓名或者昵称</li>
 *     <li>organizationId: organizationId</li>
 *     <li>userSourceType: 用户来源 1：来源app 2：来源微信</li>
 *     <li>phone: 手机号码</li>
 *     <li>startTime: 注册时间-开始</li>
 *     <li>endTime: 注册时间-结束</li>
 * </ul>
 */
public class ExportAllCommunityUsersCommand {

	private Integer namespaceId;

	private String keywords;

	private Long organizationId;

	private Byte userSourceType;

	private Long startTime;

	private Long endTime;

	private String phone;


    public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Byte getUserSourceType() {
		return userSourceType;
	}

	public void setUserSourceType(Byte userSourceType) {
		this.userSourceType = userSourceType;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
}
