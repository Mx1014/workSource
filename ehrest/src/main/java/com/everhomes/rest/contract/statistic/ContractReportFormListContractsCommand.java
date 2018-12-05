// @formatter:off
package com.everhomes.rest.contract.statistic;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 参数:
 * <li>namespaceId: 域空间id</li>
 * <li>communityId: 园区id</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页大小</li>
 * <li>keywords: 支持查询关键词 合同名称/合同编号/客户名称</li>
 * <li>contractType: 合同属性 参考{@link com.everhomes.rest.contract.ContractType}</li>
 * <li>status: 合同状态 参考{@link com.everhomes.rest.contract.ContractStatus}</li>
 * <li>customerType: 客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 * <li>updateTime: 更新时间，为空全量同步数据，不为空是增量同步（该时间点以后的数据信息）,时间戳</li>
 * <li>buildingId: 楼栋</li>
 * <li>addressId: 门牌</li>
 * <li>pageNumber: 页码</li>
 * </ul>
 */
public class ContractReportFormListContractsCommand {

	private Integer namespaceId;
	private Long communityId;
	private String keywords;
	// private Byte contractType;
	// private Byte customerType;
	private Long pageAnchor;
	private Integer pageSize;
	private Byte paymentFlag = 0;
	private Long updateTime;
	private Byte status;
	private Byte contractType;
	private Byte customerType;
	private Long addressId;
    private Long buildingId;
    private Long pageNumber;
    private String dateStr;

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public Long getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Long pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public Long getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}

	public Byte getCustomerType() {
		return customerType;
	}

	public void setCustomerType(Byte customerType) {
		this.customerType = customerType;
	}

	public Byte getContractType() {
		return contractType;
	}

	public void setContractType(Byte contractType) {
		this.contractType = contractType;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Byte getPaymentFlag() {
		return paymentFlag;
	}

	public void setPaymentFlag(Byte paymentFlag) {
		this.paymentFlag = paymentFlag;
	}

	public ContractReportFormListContractsCommand() {

	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
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
