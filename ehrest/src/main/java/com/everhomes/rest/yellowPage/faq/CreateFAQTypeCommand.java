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
 * <li>FAQTypeName: 问题分类名称</li>
 * </ul>
 **/
public class CreateFAQTypeCommand extends AllianceAdminCommand{
	
	private String FAQTypeName;

	public String getFAQTypeName() {
		return FAQTypeName;
	}

	public void setFAQTypeName(String fAQTypeName) {
		FAQTypeName = fAQTypeName;
	}
}
