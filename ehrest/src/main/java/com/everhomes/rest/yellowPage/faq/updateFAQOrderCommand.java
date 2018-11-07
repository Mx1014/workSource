package com.everhomes.rest.yellowPage.faq;

import java.util.List;

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
 * <li>FAQIds: 排好序的问题id列表</li>
 * </ul>
 **/
public class updateFAQOrderCommand extends AllianceAdminCommand{
	
	private List<Long> FAQIds;

	public List<Long> getFAQIds() {
		return FAQIds;
	}

	public void setFAQIds(List<Long> fAQIds) {
		FAQIds = fAQIds;
	}
}

