// @formatter:off
package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>communityId: 园区id</li>
 * <li>buildingName: 楼栋名称</li>
 * <li>apartmentName: 门牌名称</li>
 * <li>status: 状态，参考{@link com.everhomes.rest.organization.pm.AddressMappingStatus}</li>
 * <li>areaSize: 面积</li>
 * <li>sharedArea: 公摊面积</li>
 * <li>chargeArea: 收费面积</li>
 * <li>categoryItemId: 资产类型：住宅、写字楼、酒店式公寓、厂房、库房、车位、其他...</li>
 * <li>sourceItemId: 资产来源：自管、业主放盘、大业主交管、其他...</li>
 * <li>decorateStatus: 装修状态</li>
 * <li>orientation: 朝向</li>
 * </ul>
 */
public class CreateApartmentCommand {
	private Long communityId;
	private String buildingName;
	private String apartmentName;
	private Byte status;
	private Double areaSize;
	private Double sharedArea;
	private Double chargeArea;
	private Long categoryItemId;
	private Long sourceItemId;
	private Byte decorateStatus;
	private String orientation;

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

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
