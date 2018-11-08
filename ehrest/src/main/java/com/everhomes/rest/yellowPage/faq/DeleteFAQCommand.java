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
 * <li>FAQId: 删除的FAQ id</li>
 * </ul>
 **/
public class DeleteFAQCommand extends AllianceAdminCommand{
	
	private Long FAQId;

	public Long getFAQId() {
		return FAQId;
	}

	public void setFAQId(Long fAQId) {
		FAQId = fAQId;
	}

}
