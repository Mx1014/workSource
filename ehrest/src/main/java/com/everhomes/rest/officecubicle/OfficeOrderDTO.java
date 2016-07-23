package com.everhomes.rest.officecubicle;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *空间订单
 *<li> id: 订单id	</li>
 *<li> name: 工位空间名称	</li> 
*<li> provinceName : 省份名称	</li> 
*<li> cityName : 城市名称	</li>
*<li> address : 地址	</li>
*<li> longitude : 经度	</li>
*<li>latitude  : 纬度	</li>
*<li>contactPhone  : 咨询电话	</li>  
*<li> details : 详情-html片	</li> 
*<li> coverUrl : 封面图片url</li> 
 * <li>bannerUrls: banner图的urls </li> 
 * <li>siteType: 场所类型0开放式工位 1工位数办公室 2面积办公室 {@link com.everhomes.rest.officecubicle.OfficeSiteType}</li>
 * <li>size: 场所大小 - 对于工位是个数，对于面积是平米</li> 
 * <li>status: 状态 0客户端可见  -1客户端不可见</li> 
 * <li>orderType: 预定类别：0：参观 1：预定 </li> 
 * <li>reserveTime: 预定时间 </li> 
 * <li>reservePerson: 预订人姓名 </li> 
 * <li>reserveContact:  预订人联系方式</li> 
 * <li>reservceCompany: 预订人公司</li> 
 * </ul>
 */
public class OfficeOrderDTO {
    private Long id;
    private String name; 
    private String provinceName; 
    private String cityName;
    private String address;
    private Double longitude;
    private Double latitude;
    private String contactPhone; 
    private String details;
    private String coverUrl;
    private Byte siteType;
    private String size;
    private Byte status;
    private Byte orderType; 
    private Long reserveTime;
    private String reservePerson;
    private String reserveContact;
    private String reservceCompany;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	} 
	
	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public Byte getSiteType() {
		return siteType;
	}
	public void setSiteType(Byte siteType) {
		this.siteType = siteType;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public Byte getOrderType() {
		return orderType;
	}
	public void setOrderType(Byte orderType) {
		this.orderType = orderType;
	}
	public Long getReserveTime() {
		return reserveTime;
	}
	public void setReserveTime(Long reserveTime) {
		this.reserveTime = reserveTime;
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
