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
 * <li>FAQTypeIds: 排好序的id列表</li>
 * </ul>
 **/
public class UpdateFAQTypeOrdersCommand extends AllianceAdminCommand{
	
	List<Long> FAQTypeIds;

	public List<Long> getFAQTypeIds() {
		return FAQTypeIds;
	}

	public void setFAQTypeIds(List<Long> fAQTypeIds) {
		FAQTypeIds = fAQTypeIds;
	}
	
}
