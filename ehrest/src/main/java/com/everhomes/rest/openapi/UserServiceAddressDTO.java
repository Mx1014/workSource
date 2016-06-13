package com.everhomes.rest.openapi;

import com.everhomes.util.StringHelper;



public class UserServiceAddressDTO {
    private Long id;
    private String userName;
    private String province;
    private String city;
    private String area;
    private String callPhone;
    private String address;
    private Long communityId;
    private String communityName;
    private Byte addressType;
   
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public String getCallPhone() {
        return callPhone;
    }
    public void setCallPhone(String callPhone) {
        this.callPhone = callPhone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	public Byte getAddressType() {
		return addressType;
	}
	public void setAddressType(Byte addressType) {
		this.addressType = addressType;
	}
	@Override
    public String toString(){
        return StringHelper.toJsonString(this);
        
    }
}
