package com.everhomes.rest.yellowPage;

import java.util.List;

/**
 * <ul>
 * <li>namespaceId: namespaceId</li>
 * <li>ownerType: ownerType</li>
 * <li>ownerId: ownerId</li>
 * <li>type: 服务联盟类型</li>
 * <li>currentPMId: 当前管理公司</li>
 * <li>currentProjectId: 当前项目id</li>
 * <li>appId: 当前应用originId</li>
 * <li>serviceIds: 排好序的服务id列表</li>
 * </ul>
 **/
public class UpdateOperateServicesCommand extends AllianceAdminCommand{
	
	private List<Long> serviceIds;

	public List<Long> getServiceIds() {
		return serviceIds;
	}

	public void setServiceIds(List<Long> serviceIds) {
		this.serviceIds = serviceIds;
	}
}
