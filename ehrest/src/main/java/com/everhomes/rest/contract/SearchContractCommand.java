package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>参数:
 * <li>namespaceId: 域空间id</li>
 * <li>communityId: 园区id</li>
 * <li>keywords: 查询关键词 合同名称/合同编号/客户名称</li>
 * <li>customerType: 客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 * <li>status: 合同状态 参考{@link com.everhomes.rest.contract.ContractStatus}</li>
 * <li>contractType: 合同属性 参考{@link com.everhomes.rest.contract.ContractType}</li>
 * <li>categoryItemId: 合同类型</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页大小</li>
 * <li>sortType: 排序类型：0 升序， 1 降序</li>
 * <li>sortField: 排序字段名</li>
 * <li>categoryId: 合同类型多入口</li>
 * <li>taskId : 查询导入错误信息用，输入sync产生的taskId</li>
 * </ul>
 * Created by ying.xiong on 2017/8/17.
 */
public class SearchContractCommand {
    private Integer namespaceId;

    private Long communityId;

    private Byte customerType;

    private String keywords;

    private Long categoryItemId;

    private Byte contractType;

    private Byte status;

    private Long pageAnchor;

    private Integer pageSize;

    private Integer sortType;

    private String sortField;
    private Long orgId;
    private Long addressId;
    private Long buildingId;
    private Long categoryId;
    private Long taskId;
    private Long pageNumber;

    public Long getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Long pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
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

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    private Byte paymentFlag = 0;

    public Byte getPaymentFlag() {
        return paymentFlag;
    }

    public void setPaymentFlag(Byte paymentFlag) {
        this.paymentFlag = paymentFlag;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }

    public Byte getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Byte customerType) {
        this.customerType = customerType;
    }

    public Long getCategoryItemId() {
        return categoryItemId;
    }

    public void setCategoryItemId(Long categoryItemId) {
        this.categoryItemId = categoryItemId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Byte getContractType() {
        return contractType;
    }

    public void setContractType(Byte contractType) {
        this.contractType = contractType;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    @Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
