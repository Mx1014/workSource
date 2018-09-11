//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/8/30.
 */
/**
 *<ul>
 * <li>contractNum:合同编号</li>
 * <li>pageOffset:偏移</li>
 * <li>pageSize:页大小</li>
 *</ul>
 */
public class ListBillExpectanciesOnContractCommand {
    private String contractNum;
    private Integer pageOffset;
    private Integer pageSize;
    private Long contractId;
    private Long categoryId;
    private Integer namespaceId;
    private Long moduleId;

    public Long getCategoryId() {
		return categoryId;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getContractNum() {
        return contractNum;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
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
}
