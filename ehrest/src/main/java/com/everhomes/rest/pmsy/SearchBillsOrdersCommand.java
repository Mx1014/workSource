package com.everhomes.rest.pmsy;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>communityId : 小区ID（左邻）</li>
 * <li>startDate : 开始时间</li>
 * <li>endDate : 结束时间</li>
 * <li>userName : 用户名</li>
 * <li>userContact : 手机号</li>
 * <li>pageAnchor : 分页瞄</li>
 *</ul>
 *
 */
public class SearchBillsOrdersCommand {
	private Long communityId;
	private Long startDate;
	private Long endDate;
	private String userName;
	private String userContact;
	private Long pageAnchor;
	
	public Long getStartDate() {
		return startDate;
	}
	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}
	public Long getEndDate() {
		return endDate;
	}
	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserContact() {
		return userContact;
	}
	public void setUserContact(String userContact) {
		this.userContact = userContact;
	}
	public Long getPageAnchor() {
		return pageAnchor;
	}
	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
