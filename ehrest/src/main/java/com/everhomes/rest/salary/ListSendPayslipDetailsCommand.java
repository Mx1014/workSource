// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>organizationId: 所属id 总公司id 必填</li>
 * <li>ownerId: 分公司id 必填</li>
 * <li>salaryPeriod: 期数:类似201808 必填</li>
 * <li>payslipId: 工资条Id 必填</li>
 * <li>Name: 姓名 选填</li>
 * <li>status: 状态 不填-全部  0-已发送 1-已撤回  2-已确认 选填</li>
 * <li>pageAnchor: </li>
 * <li>pageSize: 页面大小</li>
 * </ul>
 */
public class ListSendPayslipDetailsCommand {

	private Long organizationId;

	private Long ownerId;

	private String salaryPeriod;

	private Long payslipId;
	
	private String name;
	
	private Byte status;

	private Long pageAnchor;

	private Integer pageSize;

	public ListSendPayslipDetailsCommand() {

	}

	public ListSendPayslipDetailsCommand(Long organizationId, Long ownerId, String salaryPeriod, Long payslipId) {
		super();
		this.organizationId = organizationId;
		this.ownerId = ownerId;
		this.salaryPeriod = salaryPeriod;
		this.payslipId = payslipId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getSalaryPeriod() {
		return salaryPeriod;
	}

	public void setSalaryPeriod(String salaryPeriod) {
		this.salaryPeriod = salaryPeriod;
	}

	public Long getPayslipId() {
		return payslipId;
	}

	public void setPayslipId(Long payslipId) {
		this.payslipId = payslipId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
 

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
