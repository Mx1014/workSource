package com.everhomes.rest.yellowPage.faq;

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
 * <li>upFAQId: 更新到上方的问题id</li>
 * <li>lowFAQId: 更新到下方的问题id</li>
 * </ul>
 **/
public class UpdateTopFAQOrdersCommand extends AllianceAdminCommand{
	private Long upFAQId;
	private Long lowFAQId;
	
	public Long getUpFAQId() {
		return upFAQId;
	}
	public void setUpFAQId(Long upFAQId) {
		this.upFAQId = upFAQId;
	}
	public Long getLowFAQId() {
		return lowFAQId;
	}
	public void setLowFAQId(Long lowFAQId) {
		this.lowFAQId = lowFAQId;
	}
}
