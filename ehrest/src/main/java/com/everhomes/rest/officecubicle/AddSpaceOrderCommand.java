package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *空间订单
 *<li> siteId: office site id	</li> 
 * <li>orderType: 预定类别：1-参观 2-预定 </li>  
 * <li>rentType: 租赁类别:1-开放式（默认space_type 1）,2-办公室</li>  
 * <li>spaceType: 空间类别:1-工位,2-面积 </li>  
 * <li>size: 预定空间大小</li>  
 * <li>reserverName: 预订人姓名 </li> 
 * <li>reserveContactToken:  预订人联系方式</li> 
 * <li>reserveEnterprise: 预订人公司</li> 
 * </ul>
 */
public class AddSpaceOrderCommand {

	private Long spaceId;
    private Byte orderType;
    private Byte rentType;
    private Byte spaceType;
    private Integer size;
    private String reserverName;
    private String reserveEnterprise;
    private String reserveContactToken;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
	  
	public Byte getRentType() {
		return rentType;
	}


	public void setRentType(Byte rentType) {
		this.rentType = rentType;
	}


	public Byte getSpaceType() {
		return spaceType;
	}


	public void setSpaceType(Byte spaceType) {
		this.spaceType = spaceType;
	}

 
	public Byte getOrderType() {
		return orderType;
	}


	public void setOrderType(Byte orderType) {
		this.orderType = orderType;
	}


	public Integer getSize() {
		return size;
	}


	public void setSize(Integer size) {
		this.size = size;
	}


	public Long getSpaceId() {
		return spaceId;
	}


	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}


	public String getReserverName() {
		return reserverName;
	}


	public void setReserverName(String reserverName) {
		this.reserverName = reserverName;
	}


	public String getReserveEnterprise() {
		return reserveEnterprise;
	}


	public void setReserveEnterprise(String reserveEnterprise) {
		this.reserveEnterprise = reserveEnterprise;
	}


	public String getReserveContactToken() {
		return reserveContactToken;
	}


	public void setReserveContactToken(String reserveContactToken) {
		this.reserveContactToken = reserveContactToken;
	}
 
    
}
