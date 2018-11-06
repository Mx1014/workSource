// @formatter:off
package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>id: 家庭Id</li>
 * 	<li>name: 家庭名称</li>
 * 	<li>memberCount: 家庭成员数</li>
 * 	<li>address: 家庭所在地址详情</li>
 * 	<li>addressId: 地址信息</li>
 * 	<li>livingStatus: 地址状态, 详见{@link com.everhomes.rest.organization.pm.PmMemberStatus}</li>
 * 	<li>owed: 是否欠费 , 详见{@link com.everhomes.rest.organization.pm.OwedType}</li>
 * 	<li>reservationInvolved: 是否被预定业务占用</li>
 * 	<li>buildingName: 楼栋名称</li>
 * 	<li>communityName: 项目名称</li>
 * <li>apartAuthorizePrice: 房源授权价</li>
 * </ul>
 */
public class PropFamilyDTO {
    private Long id;
    private String name;
    private Long memberCount;
    private Long addressId;
    private String address;
    private String apartmentFloor;
    private Byte livingStatus;
    private String enterpriseName;
    private Double areaSize;
    private Double rentArea;
    private Double freeArea;
    private Double chargeArea;
    private Byte reservationInvolved;
    private Long relatedContractEndDate;
    private Long relatedAddressArrangementBeginDate;
    private Byte isFutureApartment;
    private Byte arrangementInvolved;
    private String buildingName;
    private String communityName;
    private Long reservationId;
    private String apartAuthorizePrice;
    
	public String getApartAuthorizePrice() {
		return apartAuthorizePrice;
	}

	public void setApartAuthorizePrice(String apartAuthorizePrice) {
		this.apartAuthorizePrice = apartAuthorizePrice;
	}

	public Long getReservationId() {
		return reservationId;
	}

	public void setReservationId(Long reservationId) {
		this.reservationId = reservationId;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public Byte getArrangementInvolved() {
		return arrangementInvolved;
	}

	public void setArrangementInvolved(Byte arrangementInvolved) {
		this.arrangementInvolved = arrangementInvolved;
	}

	public Double getRentArea() {
		return rentArea;
	}

	public void setRentArea(Double rentArea) {
		this.rentArea = rentArea;
	}

	public Double getFreeArea() {
		return freeArea;
	}

	public void setFreeArea(Double freeArea) {
		this.freeArea = freeArea;
	}

	public Double getChargeArea() {
		return chargeArea;
	}

	public void setChargeArea(Double chargeArea) {
		this.chargeArea = chargeArea;
	}

	public Long getRelatedContractEndDate() {
		return relatedContractEndDate;
	}

	public void setRelatedContractEndDate(Long relatedContractEndDate) {
		this.relatedContractEndDate = relatedContractEndDate;
	}

	public Byte getIsFutureApartment() {
		return isFutureApartment;
	}

	public void setIsFutureApartment(Byte isFutureApartment) {
		this.isFutureApartment = isFutureApartment;
	}

	public Long getRelatedAddressArrangementBeginDate() {
		return relatedAddressArrangementBeginDate;
	}

	public void setRelatedAddressArrangementBeginDate(Long relatedAddressArrangementBeginDate) {
		this.relatedAddressArrangementBeginDate = relatedAddressArrangementBeginDate;
	}

	public Byte getReservationInvolved() {
		return reservationInvolved;
	}

	public void setReservationInvolved(Byte reservationInvolved) {
		this.reservationInvolved = reservationInvolved;
	}

	private Byte owed;
    
    public PropFamilyDTO () {
    }

	public String getApartmentFloor() {
		return apartmentFloor;
	}

	public void setApartmentFloor(String apartmentFloor) {
		this.apartmentFloor = apartmentFloor;
	}

	public Byte getOwed() {
		return owed;
	}

	public void setOwed(Byte owed) {
		this.owed = owed;
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

    public Long getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(Long memberCount) {
		this.memberCount = memberCount;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public Byte getLivingStatus() {
		return livingStatus;
	}

	public void setLivingStatus(Byte livingStatus) {
		this.livingStatus = livingStatus;
	}
	
	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public Double getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(Double areaSize) {
		this.areaSize = areaSize;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
