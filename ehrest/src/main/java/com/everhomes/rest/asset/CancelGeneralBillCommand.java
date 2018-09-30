//@formatter:off
package com.everhomes.rest.asset;

/**
 *<ul>
 * <li>namespaceId:域空间ID</li>
 * <li>ownerType:所属者type</li>
 * <li>ownerId:所属者id</li>
 * <li>sourceType:各个业务系统定义的唯一标识</li>
 * <li>sourceId:各个业务系统定义的唯一标识</li>
 * <li>billId:统一账单id</li>
 * <li>thirdBillId:各个业务系统定义的唯一账单标识</li>
 *</ul>
 */
public class CancelGeneralBillCommand {
	private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private String sourceType;
    private Long sourceId;
    private Long billId;
    private String thirdBillId;
    
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
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public Long getSourceId() {
		return sourceId;
	}
	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}
	public Long getBillId() {
		return billId;
	}
	public void setBillId(Long billId) {
		this.billId = billId;
	}
	public String getThirdBillId() {
		return thirdBillId;
	}
	public void setThirdBillId(String thirdBillId) {
		this.thirdBillId = thirdBillId;
	}
    
}
