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
 * <li>upServiceId: 更换到上方的服务id</li>
 * <li>lowServiceId: 更换到下方的服务id</li>
 * </ul>
 **/
public class UpdateOperateServiceOrdersCommand extends AllianceAdminCommand{
	private Long upServiceId;
	private Long lowServiceId;
	public Long getUpServiceId() {
		return upServiceId;
	}
	public void setUpServiceId(Long upServiceId) {
		this.upServiceId = upServiceId;
	}
	public Long getLowServiceId() {
		return lowServiceId;
	}
	public void setLowServiceId(Long lowServiceId) {
		this.lowServiceId = lowServiceId;
	}
}
