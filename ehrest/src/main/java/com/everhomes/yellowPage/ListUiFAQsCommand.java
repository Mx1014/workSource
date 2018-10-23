package com.everhomes.yellowPage;

import com.everhomes.rest.yellowPage.AllianceCommonCommand;

/**
 * <ul>
 * <li>namespaceId: namespaceId</li>
 * <li>ownerType: ownerType</li>
 * <li>ownerId: ownerId</li>
 * <li>type: 服务联盟类型</li>
 * <li>FAQTypeId: 问题分类id</li>
 * <li>FAQId: 问题id</li>
 * <li>pageAnchor:锚点</li>
 * <li>pageSize: 页码</li>
 * </ul>
 **/
public class ListUiFAQsCommand extends AllianceCommonCommand{
	private Long FAQTypeId;
	private Long FAQId;
	private Long pageAnchor;
	private Integer pageSize;
	
	public Long getFAQTypeId() {
		return FAQTypeId;
	}
	public void setFAQTypeId(Long fAQTypeId) {
		FAQTypeId = fAQTypeId;
	}
	public Long getFAQId() {
		return FAQId;
	}
	public void setFAQId(Long fAQId) {
		FAQId = fAQId;
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
