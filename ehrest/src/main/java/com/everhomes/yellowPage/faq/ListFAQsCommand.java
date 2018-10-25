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
 * <li>pageAnchor:锚点</li>
 * <li>pageSize: 页码</li>
 * <li>FAQType: 问题类型</li>
 * <li>topFlag: 空-都显示 0-非热门 1-热门</li>
 * <li>keyword: 关键字</li>
 * <li>orderType: 空-按默认排序 0-以解决次数 1-以未解决次数</li>
 * <li>sortType: 空或<0-降序  >0-升序</li>
 * </ul>
 **/
public class ListFAQsCommand extends AllianceAdminCommand{
	private Long pageAnchor;
	private Integer pageSize;
	private Long FAQType;
	private Byte topFlag;
	private String keyword;
	private Byte orderType;
	private Byte sortType;
	
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
	public Long getFAQType() {
		return FAQType;
	}
	public void setFAQType(Long fAQType) {
		FAQType = fAQType;
	}
	public Byte getTopFlag() {
		return topFlag;
	}
	public void setTopFlag(Byte topFlag) {
		this.topFlag = topFlag;
	}
	public Byte getOrderType() {
		return orderType;
	}
	public void setOrderType(Byte orderType) {
		this.orderType = orderType;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Byte getSortType() {
		return sortType;
	}
	public void setSortType(Byte sortType) {
		this.sortType = sortType;
	}
}
