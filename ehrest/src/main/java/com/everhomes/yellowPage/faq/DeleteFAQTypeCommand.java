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
 * <li>FAQTypeId: 问题分类id</li>
 * </ul>
 **/
public class DeleteFAQTypeCommand extends AllianceAdminCommand{
	private Long FAQTypeId;

	public Long getFAQTypeId() {
		return FAQTypeId;
	}

	public void setFAQTypeId(Long fAQTypeId) {
		FAQTypeId = fAQTypeId;
	}
	
}
