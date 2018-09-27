// @formatter:off
package com.everhomes.rest.address;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>namespaceId: namespaceId</li>
 * 	<li>communityId:communityId</li>
 * 	<li>buildingId: 楼栋id</li>
 * 	<li>addressName: 房源名称</li>
 * 	<li>addressId: 地址信息</li>
 * 	<li>chargingItemsId:收费项id</li>
 * 	<li>chargingItemsName:收费项名称</li>
 * 	<li>authorizePrice:授权价</li>
 * 	<li>apartmentAuthorizeType:授权价类型</li>
 * </ul>
 */
public class AuthorizePriceDTO {
	private Long id;
    private Integer namespaceId;
	private Long communityId;
	private Long buildingId;
	private Long addressId;
	private String addressName;
	private Long chargingItemsId;
	private String chargingItemsName;
	private BigDecimal authorizePrice;
	private Byte apartmentAuthorizeType;
	private Long creatorUid;
    private String creatorName;
    private Timestamp createTime;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Long getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public Long getChargingItemsId() {
		return chargingItemsId;
	}

	public void setChargingItemsId(Long chargingItemsId) {
		this.chargingItemsId = chargingItemsId;
	}

	public String getChargingItemsName() {
		return chargingItemsName;
	}

	public void setChargingItemsName(String chargingItemsName) {
		this.chargingItemsName = chargingItemsName;
	}

	public BigDecimal getAuthorizePrice() {
		return authorizePrice;
	}

	public void setAuthorizePrice(BigDecimal authorizePrice) {
		this.authorizePrice = authorizePrice;
	}

	public Byte getApartmentAuthorizeType() {
		return apartmentAuthorizeType;
	}

	public void setApartmentAuthorizeType(Byte apartmentAuthorizeType) {
		this.apartmentAuthorizeType = apartmentAuthorizeType;
	}

	public Long getCreatorUid() {
		return creatorUid;
	}

	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
