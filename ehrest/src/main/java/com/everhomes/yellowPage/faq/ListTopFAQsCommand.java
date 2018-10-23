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
 * <li>sourceRequestType: 0-客户端/微信/H5 其他，包括空-后台</li>
 * <li>pageAnchor:锚点</li>
 * <li>pageSize: 页码</li>
 * </ul>
 **/
public class ListTopFAQsCommand extends AllianceAdminCommand{
	
	private Byte sourceRequestType;
	private Long pageAnchor;
	private Integer pageSize;
	public Byte getSourceRequestType() {
		return sourceRequestType;
	}

	public void setSourceRequestType(Byte sourceRequestType) {
		this.sourceRequestType = sourceRequestType;
	}

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
