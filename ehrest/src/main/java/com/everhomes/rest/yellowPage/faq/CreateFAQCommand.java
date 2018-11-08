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
 * <li>title : 问题标题</li>
 * <li>content : 内容</li>
 * <li>typeId : 类型id</li>
 * </ul>
 **/
public class CreateFAQCommand extends AllianceAdminCommand{
	private String title;
	private String content;
	private Long typeId;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getTypeId() {
		return typeId;
	}
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

}
