//@formatter:off
package com.everhomes.rest.asset.bill;

import java.util.List;

/**
 *<ul>
 * <li>namespaceId:域空间ID</li>
 * <li>ownerId:所属者ID</li>
 * <li>ownerType:所属者类型</li>
 * <li>contractIdList:合同ID列表</li>
 *</ul>
 */
public class BatchDeleteBillFromContractCmd {
	private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private List<Long> contractIdList;
    
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public List<Long> getContractIdList() {
		return contractIdList;
	}
	public void setContractIdList(List<Long> contractIdList) {
		this.contractIdList = contractIdList;
	}

}
