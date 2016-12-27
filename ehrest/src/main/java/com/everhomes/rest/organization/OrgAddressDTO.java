package com.everhomes.rest.organization;

public class OrgAddressDTO {
	
	private Long     organizationId;
	private String   displayName;
	
	private java.lang.Long     addressId;
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
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public java.lang.Long getAddressId() {
		return addressId;
	}
	public void setAddressId(java.lang.Long addressId) {
		this.addressId = addressId;
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
    
}
