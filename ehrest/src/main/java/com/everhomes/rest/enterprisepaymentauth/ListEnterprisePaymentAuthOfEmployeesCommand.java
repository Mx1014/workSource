// @formatter:off
package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 总公司id</li>
 * <li>departmentId: 查询部门id</li>
 * <li>paymentSceneAppId: 支付应用场景id</li>
 * <li>keyWords: 员工姓名/电话</li>
 * <li>pageOffset: 页码，默认为1</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListEnterprisePaymentAuthOfEmployeesCommand {
	private Long organizationId;
	private Long departmentId;
	private Long paymentSceneAppId;
	private String keyWords;
	private Integer pageOffset;
	private Integer pageSize;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getPaymentSceneAppId() {
		return paymentSceneAppId;
	}

	public void setPaymentSceneAppId(Long paymentSceneAppId) {
		this.paymentSceneAppId = paymentSceneAppId;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public Integer getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
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
