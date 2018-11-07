package com.everhomes.rest.yellowPage.faq;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id : id</li>
 * <li>userId : 客服id</li>
 * <li>userName : 客服名称</li>
 * <li>hotlineNumber : 热线电话</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年10月23日
 */
public class GetFAQOnlineServiceResponse {
	private Long id;
	private Long userId;
	private String userName;
	private String hotlineNumber;
	
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHotlineNumber() {
		return hotlineNumber;
	}

	public void setHotlineNumber(String hotlineNumber) {
		this.hotlineNumber = hotlineNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
