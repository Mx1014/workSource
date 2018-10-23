package com.everhomes.yellowPage.faq;

import com.everhomes.rest.yellowPage.AllianceAdminCommand;
/**
 * <ul>
 * <li>namespaceId: namespaceId</li>
 * <li>ownerType: ownerType</li>
 * <li>ownerId: ownerId</li>
 * <li>type: 服务联盟类型</li>
 * <li>currentPMId: 当前管理公司</li>
 * <li>currentProjectId: 当前项目id</li>
 * <li>appId: 当前应用originId</li>
 * <li>upFAQTypeId: 上方的id</li>
 * <li>lowFAQTypeId: 下方的id</li>
 * </ul>
 **/
public class UpdateFAQTypeOrdersCommand extends AllianceAdminCommand{
	
	private Long upFAQTypeId;
	private Long lowFAQTypeId;
	
	public Long getUpFAQTypeId() {
		return upFAQTypeId;
	}
	public void setUpFAQTypeId(Long upFAQTypeId) {
		this.upFAQTypeId = upFAQTypeId;
	}
	public Long getLowFAQTypeId() {
		return lowFAQTypeId;
	}
	public void setLowFAQTypeId(Long lowFAQTypeId) {
		this.lowFAQTypeId = lowFAQTypeId;
	}
}
