// @formatter:off
package com.everhomes.rest.address;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 *
 * <ul>
 * <li>namespaceId: 域空间id，用于权限校验</li>
 * <li>organizationId: 管理公司id，用于权限校验</li>
 * <li>communityId: 园区id，用于权限校验</li>
 * <li>id: 门牌id</li>
 * <li>apartmentName: 门牌名称</li>
 * <li>status: 状态，参考{@link com.everhomes.rest.organization.pm.AddressMappingStatus}</li>
 * <li>areaSize: 面积</li>
 * <li>sharedArea: 公摊面积</li>
 * <li>chargeArea: 收费面积</li>
 * <li>buildArea: 建筑面积</li>
 * <li>rentArea: 出租面积</li>
 * <li>categoryItemId: 资产类型：住宅、写字楼、酒店式公寓、厂房、库房、车位、其他...</li>
 * <li>sourceItemId: 资产来源：自管、业主放盘、大业主交管、其他...</li>
 * <li>decorateStatus: 装修状态</li>
 * <li>orientation: 朝向</li>
 * <li>apartmentFloor: 房源所在楼层</li>
 * <li>freeArea: 可招租面积</li>
 * <li>apartmentRent: 房源的租金</li>
 * <li>apartmentRentType: 房源的租金类型</li>
 * </ul>
 */
public class UpdateApartmentCommand {
	
	private Integer namespaceId;
	private Long organizationId;
	private Long communityId;
	private Long id;
	private String apartmentName;
	private Byte status;
	private Double areaSize;
	private Double sharedArea;
	private Double chargeArea;
	private Double buildArea;
	private Double rentArea;
	private Long categoryItemId;
	private Long sourceItemId;
	private Byte decorateStatus;
	private String orientation;
	private String apartmentFloor;
	private Double freeArea;
	private BigDecimal apartmentRent;
	private Byte apartmentRentType;
	
	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public BigDecimal getApartmentRent() {
		return apartmentRent;
	}

	public void setApartmentRent(BigDecimal apartmentRent) {
		this.apartmentRent = apartmentRent;
	}

	public Byte getApartmentRentType() {
		return apartmentRentType;
	}

	public void setApartmentRentType(Byte apartmentRentType) {
		this.apartmentRentType = apartmentRentType;
	}

	public Double getFreeArea() {
		return freeArea;
	}

	public void setFreeArea(Double freeArea) {
		this.freeArea = freeArea;
	}

	private Long ownerId;

	public String getApartmentFloor() {
		return apartmentFloor;
	}

	public void setApartmentFloor(String apartmentFloor) {
		this.apartmentFloor = apartmentFloor;
	}

	public Double getBuildArea() {
		return buildArea;
	}

	public void setBuildArea(Double buildArea) {
		this.buildArea = buildArea;
	}

	public Double getRentArea() {
		return rentArea;
	}

	public void setRentArea(Double rentArea) {
		this.rentArea = rentArea;
	}

	public Long getCategoryItemId() {
		return categoryItemId;
	}

	public void setCategoryItemId(Long categoryItemId) {
		this.categoryItemId = categoryItemId;
	}

	public Double getChargeArea() {
		return chargeArea;
	}

	public void setChargeArea(Double chargeArea) {
		this.chargeArea = chargeArea;
	}

	public Byte getDecorateStatus() {
		return decorateStatus;
	}

	public void setDecorateStatus(Byte decorateStatus) {
		this.decorateStatus = decorateStatus;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public Double getSharedArea() {
		return sharedArea;
	}

	public void setSharedArea(Double sharedArea) {
		this.sharedArea = sharedArea;
	}

	public Long getSourceItemId() {
		return sourceItemId;
	}

	public void setSourceItemId(Long sourceItemId) {
		this.sourceItemId = sourceItemId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApartmentName() {
		return apartmentName;
	}

	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Double getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(Double areaSize) {
		this.areaSize = areaSize;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
