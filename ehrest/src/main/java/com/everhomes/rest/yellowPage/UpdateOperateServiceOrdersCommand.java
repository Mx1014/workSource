package com.everhomes.rest.yellowPage;

/**
 * <ul>
 * <li>namespaceId: namespaceId</li>
 * <li>ownerType: ownerType</li>
 * <li>ownerId: ownerId</li>
 * <li>type: 服务联盟类型</li>
 * <li>currentPMId: 当前管理公司</li>
 * <li>currentProjectId: 当前项目id</li>
 * <li>appId: 当前应用originId</li>
 * <li>upOperateServiceId: 更换到上方的id</li>
 * <li>lowOperateServiceId: 更换到下方的id</li>
 * </ul>
 **/
public class UpdateOperateServiceOrdersCommand extends AllianceAdminCommand{
	private Long upOperateServiceId;
	private Long lowOperateServiceId;
	
	public Long getUpOperateServiceId() {
		return upOperateServiceId;
	}
	public void setUpOperateServiceId(Long upOperateServiceId) {
		this.upOperateServiceId = upOperateServiceId;
	}
	public Long getLowOperateServiceId() {
		return lowOperateServiceId;
	}
	public void setLowOperateServiceId(Long lowOperateServiceId) {
		this.lowOperateServiceId = lowOperateServiceId;
	}
}
