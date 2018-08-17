package com.everhomes.rest.yellowPage.stat;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>namespaceId: </li>
 * <li>communityId: 项目id</li>
 * <li>clickTime: 点击时间戳</li>
 * <li>serviceId: 服务id</li>
 * <li>categoryId: 类型id</li>
 * <li>userId: 用户id</li>
 * <li>clickType: 点击类型</li>
 * </ul>
 */
public class TestAddClickDetailCommand {

	private Integer namespaceId;
	
	private Long communityId;
	
	private Long clickTime;
	
	private Long serviceId;
	
	private Long categoryId;
	
	private Long userId;
	
	private Byte clickType;
	

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getClickTime() {
		return clickTime;
	}

	public void setClickTime(Long clickTime) {
		this.clickTime = clickTime;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Byte getClickType() {
		return clickType;
	}

	public void setClickType(Byte clickType) {
		this.clickType = clickType;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

}
