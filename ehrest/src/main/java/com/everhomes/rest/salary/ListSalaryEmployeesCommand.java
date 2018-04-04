// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>keywords: 搜索关键词</li>
 * <li>checkInMonth: 入职月份时间戳</li>
 * <li>dismissMonth: 离职月份时间戳</li>
 * <li>ownerType: 'organization'</li>
 * <li>ownerId: 子公司organizationId</li>
 * <li>salaryStatus: 0-正常 -1 异常</li>
 * <li>pageAnchor: </li>
 * <li>pageSize: 页面大小</li>
 * </ul>
 */
public class ListSalaryEmployeesCommand {

	private String keywords;

	private Integer namespaceId;

	private String ownerType;

	private Long ownerId;

	private Long checkInMonth;

	private Long dismissMonth;

	private Byte salaryStatus;

	private Long pageAnchor;

	private Integer pageSize;

	public ListSalaryEmployeesCommand() {

	}

	public Long getCheckInMonth() {
		return checkInMonth;
	}

	public void setCheckInMonth(Long checkInMonth) {
		this.checkInMonth = checkInMonth;
	}

	public Long getDismissMonth() {
		return dismissMonth;
	}

	public void setDismissMonth(Long dismissMonth) {
		this.dismissMonth = dismissMonth;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
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

	public Byte getSalaryStatus() {
		return salaryStatus;
	}

	public void setSalaryStatus(Byte salaryStatus) {
		this.salaryStatus = salaryStatus;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
}
