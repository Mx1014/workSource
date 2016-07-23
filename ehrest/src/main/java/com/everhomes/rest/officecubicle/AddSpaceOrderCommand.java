package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *空间订单
 *<li> siteId: office site id	</li> 
 * <li>orderType: 预定类别：0：参观 1：预定 </li>  
 * <li>reservePerson: 预订人姓名 </li> 
 * <li>reserveContact:  预订人联系方式</li> 
 * <li>reservceCompany: 预订人公司</li> 
 * </ul>
 */
public class AddSpaceOrderCommand {

	private Long siteId;
    private Byte orderType;
    private String reservePerson;
    private String reserveContact;
    private String reservceCompany;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
	
	public Long getSiteId() {
		return siteId;
	}
	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
	public Byte getOrderType() {
		return orderType;
	}
	public void setOrderType(Byte orderType) {
		this.orderType = orderType;
	}
	public String getReservePerson() {
		return reservePerson;
	}
	public void setReservePerson(String reservePerson) {
		this.reservePerson = reservePerson;
	}
	public String getReserveContact() {
		return reserveContact;
	}
	public void setReserveContact(String reserveContact) {
		this.reserveContact = reserveContact;
	}
	public String getReservceCompany() {
		return reservceCompany;
	}
	public void setReservceCompany(String reservceCompany) {
		this.reservceCompany = reservceCompany;
	}
    
}
