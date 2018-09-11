package com.everhomes.rest.yellowPage.stat;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userName:用户姓名</li>
 * <li>userPhone: 联系电话</li>
 * <li>clickTypeName: 用户点击行为名称</li>
 * <li>serviceName: 服务名称</li>
 * <li>serviceTypeName: 服务类型</li>
 * <li>clickTime: 点击/操作时间戳</li>
 * </ul>
 */
public class ClickStatDetailDTO {
	
	private String userName;
	private String userPhone;
	private String clickTypeName;
	private String serviceName;
	private String serviceTypeName;
	private Long clickTime;
	private String clickShowTime;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getClickTypeName() {
		return clickTypeName;
	}

	public void setClickTypeName(String clickTypeName) {
		this.clickTypeName = clickTypeName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Long getClickTime() {
		return clickTime;
	}

	public void setClickTime(Long clickTime) {
		this.clickTime = clickTime;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getServiceTypeName() {
		return serviceTypeName;
	}

	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}

	public String getClickShowTime() {
		return clickShowTime;
	}

	public void setClickShowTime(String clickShowTime) {
		this.clickShowTime = clickShowTime;
	}

}
