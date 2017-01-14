package com.everhomes.rest.servicehotline;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 *  
 * <li>namespaceId: 域空间</li>  
 * <li>ownerType: community </li>  
 * <li>ownerId: ownerId </li>  
 * <li>serviceType:类型</li>  
 * <li>name: 热线名</li>  
 * <li>contact: 联系电话</li>  
 * <li>userId: 用户id</li>   
 * <li>description: 备注 </li>   
 * <li>defaultOrder: 排序字段</li>   
 * <li>avatar: 头像</li>   
 * </ul>
 */
public class AddHotlineCommand { 
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private Integer serviceType;
    private String name;
    private String contact;
    private Long userId;
    private String description;
    private Integer defaultOrder;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    } 
	 
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
	public Integer getServiceType() {
		return serviceType;
	}
	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getDefaultOrder() {
		return defaultOrder;
	}
	public void setDefaultOrder(Integer defaultOrder) {
		this.defaultOrder = defaultOrder;
	} 
    
    
}
