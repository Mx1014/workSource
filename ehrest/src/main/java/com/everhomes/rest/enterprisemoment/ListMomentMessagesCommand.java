// @formatter:off
package com.everhomes.rest.enterprisemoment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 公司id</li>
 * <li>newMessageFlag: 查看新消息flag:0/null - 查所有消息 1- 查新消息</li>
 * <li>pageAnchor:  锚点</li>
 * <li>pageSize: 单页数量 首次进入用列表的新消息数量</li>
 * </ul>
 */
public class ListMomentMessagesCommand {
	private Long organizationId;
	private Byte newMessageFlag;
	private Long pageAnchor;
	private Integer pageSize;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Byte getNewMessageFlag() {
		return newMessageFlag;
	}

	public void setNewMessageFlag(Byte newMessageFlag) {
		this.newMessageFlag = newMessageFlag;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
