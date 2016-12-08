package com.everhomes.rest.servicehotline;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 * <ul> 
 * 
 * <li>ownerType: 一般是community</li>  
 * <li>ownerId: ownerId</li>  
 * <li>pageAnchor: 不解释</li>  
 * <li>pageSize: 不解释</li>  
 * <li>serviceType: topic的serviceType</li>   
 * </ul>
 */
public class GetHotlineListCommand {
 
	
	private String ownerType;
	@NotNull
	private Long ownerId; 
	
	private Long pageAnchor;
    
	private Integer pageSize;

	private Byte  serviceType;

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

	public Byte getServiceType() {
		return serviceType;
	}

	public void setServiceType(Byte serviceType) {
		this.serviceType = serviceType;
	}
	

	 
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    } 
}
