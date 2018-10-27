package com.everhomes.yellowPage.faq;

import com.everhomes.rest.yellowPage.AllianceAdminCommand;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: namespaceId</li>
 * <li>ownerType: ownerType</li>
 * <li>ownerId: ownerId</li>
 * <li>type: 服务联盟类型</li>
 * <li>currentPMId: 当前管理公司</li>
 * <li>currentProjectId: 当前项目id</li>
 * <li>appId: 当前应用originId</li>
 * <li>id: id</li>
 * <li>userId: 客服用户id</li>
 * <li>userName: 客服用户名称</li>
 * <li>hotlineNumber: 客服热线</li>
 * </ul>
 **/
public class UpdateFAQOnlineServiceCommand extends AllianceAdminCommand{
	private String id;
	private String userId;
	private String userName;
	private String hotlineNumber;
	
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
