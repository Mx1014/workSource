package com.everhomes.rest.servicehotline;

 
/**
 * <ul> 
 *  
 * <li>ownerType: community </li>  
 * <li>ownerId: ownerId </li>  
 * <li>serviceType:类型</li>  
 * <li>switchFlag:  0-close  1-open</li>   
 * <li>displayName: 显示名称 如"专属客服"</li>
 * <li>namespaceId: 域空间Id</li>
 * </ul>
 */
public class SetHotlineSubjectCommand {
    private String ownerType;
    private Long ownerId;
    private Integer serviceType;
    private Byte switchFlag;
    private String displayName;
	private Integer namespaceId;
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
	public Integer getServiceType() {
		return serviceType;
	}
	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}
	public Byte getSwitchFlag() {
		return switchFlag;
	}
	public void setSwitchFlag(Byte switchFlag) {
		this.switchFlag = switchFlag;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
}
