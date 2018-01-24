// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>keywords: 搜索关键词</li>
 * <li>checkInMonth: 入职月份</li>
 * <li>dismissMonth: 离职月份</li>
 * <li>ownerType: 'organization'</li>
 * <li>ownerId: 子公司organizationId</li>
 * <li>salaryStatus: 0-正常 -1 异常</li>
 * <li>pageAnchor: </li>
 * <li>pageSize: 页面大小</li>
 * </ul>
 */
public class ListSalaryEmployeesCommand {

	private String keywords;

	private String ownerType;

	private Long ownerId;

	private String checkInMonth;

	private String dismissMonth;

	private Byte salaryStatus;

	private Long pageAnchor;

	private Integer pageSize;

	public ListSalaryEmployeesCommand() {

	}

	public String getCheckInMonth() {
		return checkInMonth;
	}

	public void setCheckInMonth(String checkInMonth) {
		this.checkInMonth = checkInMonth;
	}

	public String getDismissMonth() {
		return dismissMonth;
	}

	public void setDismissMonth(String dismissMonth) {
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
}
