package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>uuid: uuid</li>
 *     <li>communityId: 小区id</li>
 *     <li>cityId: 城市id</li>
 *     <li>address: 地址</li>
 *     <li>longitude: 经度</li>
 *     <li>latitude: 纬度</li>
 *     <li>addressAlias: 地址别名</li>
 *     <li>buildingName: 楼栋名称</li>
 *     <li>buildingAliasName: 楼栋别名</li>
 *     <li>apartmentName: 门牌名称</li>
 *     <li>apartmentFloor: 楼层</li>
 * </ul>
 */
public class AddressDTO {
    private java.lang.Long     id;
    private java.lang.String   uuid;
    private java.lang.Long     communityId;
    private java.lang.Long     cityId;
    private java.lang.String   zipcode;
    private java.lang.String   address;
    private java.lang.Double   longitude;
    private java.lang.Double   latitude;
    private java.lang.String   geohash;
    private java.lang.String   addressAlias;
    private java.lang.String   buildingName;
    private java.lang.String   buildingAliasName;
    private java.lang.String   apartmentName;
    private java.lang.String   apartmentFloor;
    private java.lang.Byte     status;
    private java.lang.Long     creatorUid;
    private java.sql.Timestamp createTime;
    private java.sql.Timestamp deleteTime;
    private Byte memberStatus;
    private Double areaSize;

    public Double getAreaSize() {
        return areaSize;
    }

    public void setAreaSize(Double areaSize) {
        this.areaSize = areaSize;
    }

    public AddressDTO() {
    }

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.String getUuid() {
        return uuid;
    }

    public void setUuid(java.lang.String uuid) {
        this.uuid = uuid;
    }

    public java.lang.Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(java.lang.Long communityId) {
        this.communityId = communityId;
    }

    public java.lang.Long getCityId() {
        return cityId;
    }

    public void setCityId(java.lang.Long cityId) {
        this.cityId = cityId;
    }

    public java.lang.String getZipcode() {
        return zipcode;
    }

    public void setZipcode(java.lang.String zipcode) {
        this.zipcode = zipcode;
    }

    public java.lang.String getAddress() {
        return address;
    }

    public void setAddress(java.lang.String address) {
        this.address = address;
    }

    public java.lang.Double getLongitude() {
        return longitude;
    }

    public void setLongitude(java.lang.Double longitude) {
        this.longitude = longitude;
    }

    public java.lang.Double getLatitude() {
        return latitude;
    }

    public void setLatitude(java.lang.Double latitude) {
        this.latitude = latitude;
    }

    public java.lang.String getGeohash() {
        return geohash;
    }

    public void setGeohash(java.lang.String geohash) {
        this.geohash = geohash;
    }

    public java.lang.String getAddressAlias() {
        return addressAlias;
    }

    public void setAddressAlias(java.lang.String addressAlias) {
        this.addressAlias = addressAlias;
    }

    public java.lang.String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(java.lang.String buildingName) {
        this.buildingName = buildingName;
    }

    public java.lang.String getBuildingAliasName() {
        return buildingAliasName;
    }

    public void setBuildingAliasName(java.lang.String buildingAliasName) {
        this.buildingAliasName = buildingAliasName;
    }

    public java.lang.String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(java.lang.String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public java.lang.String getApartmentFloor() {
        return apartmentFloor;
    }

    public void setApartmentFloor(java.lang.String apartmentFloor) {
        this.apartmentFloor = apartmentFloor;
    }

    public java.lang.Byte getStatus() {
        return status;
    }

    public void setStatus(java.lang.Byte status) {
        this.status = status;
    }

    public java.lang.Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(java.lang.Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public java.sql.Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.sql.Timestamp createTime) {
        this.createTime = createTime;
    }

    public java.sql.Timestamp getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(java.sql.Timestamp deleteTime) {
        this.deleteTime = deleteTime;
    }
    
    
    public Byte getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(Byte memberStatus) {
		this.memberStatus = memberStatus;
	}

    @Override
    public String toString(){
        return StringHelper.toJsonString(this);
        
    }
}
