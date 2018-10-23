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
 * <li>FAQId: 问题id</li>
 * <li>topFlag: 0-取消热门 1-设置为热门</li>
 * </ul>
 **/
public class UpdateTopFAQFlagCommand extends AllianceAdminCommand{
	
	private Long FAQId;
	private Byte topFlag;
	
	public Long getFAQId() {
		return FAQId;
	}
	public void setFAQId(Long fAQId) {
		FAQId = fAQId;
	}
	public Byte getTopFlag() {
		return topFlag;
	}
	public void setTopFlag(Byte topFlag) {
		this.topFlag = topFlag;
	}
}
