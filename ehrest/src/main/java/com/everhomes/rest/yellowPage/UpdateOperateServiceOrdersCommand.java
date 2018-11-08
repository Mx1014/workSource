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
 * <li>operateServiceIds: 排好顺序的id列表</li>
 * </ul>
 **/
public class UpdateOperateServiceOrdersCommand extends AllianceAdminCommand{
	
	private List<Long> operateServiceIds;

	public List<Long> getOperateServiceIds() {
		return operateServiceIds;
	}

	public void setOperateServiceIds(List<Long> operateServiceIds) {
		this.operateServiceIds = operateServiceIds;
	}
}
