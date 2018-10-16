// @formatter:off
package com.everhomes.rest.address;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区Id</li>
 * <li>namespaceId: namespaceId</li>
 * <li>addressId: 地址Id</li>
 * <li>buildingId: 楼栋ID</li>
 * <li>chargingItemsId: 费项id</li>
 * <li>authorizePrice: 授权价</li>
 * <li>apartmentAuthorizeType: 授权价单位(1:每天; 2:每月; 3:每个季度; 4:每年;)</li>
 * </ul>
 */
public class AuthorizePriceCommand {
	private Long id;
	private Integer namespaceId;
	private Long communityId;
	private Long buildingId;
	private Long addressId;
	private Long chargingItemsId;
	private BigDecimal authorizePrice;
	private Byte apartmentAuthorizeType;
	private Long pageAnchor = 0L;
	private Integer pageSize;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getChargingItemsId() {
		return chargingItemsId;
	}

	public void setChargingItemsId(Long chargingItemsId) {
		this.chargingItemsId = chargingItemsId;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
