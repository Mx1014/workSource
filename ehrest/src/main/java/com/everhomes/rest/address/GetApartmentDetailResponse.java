// @formatter:off
package com.everhomes.rest.address;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationOwnerDTO;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>buildingName: 楼栋名称</li>
 * <li>apartmentName: 门牌名称</li>
 * <li>status: 状态</li>
 * <li>areaSize: 面积</li>
 * <li>enterpriseName: 企业名称，园区有小区无</li>
 * <li>owerList: 用户列表，园区无小区有,,参考{@link com.everhomes.rest.organization.OrganizationOwnerDTO}</li>
 * <li>nextPageAnchor: 下页锚点，园区无小区有</li>
 * <li>sharedArea: 公摊面积</li>
 * <li>chargeArea: 收费面积</li>
 * <li>buildArea: 建筑面积</li>
 * <li>rentArea: 出租面积</li>
 * <li>categoryItemId: 资产类型：住宅、写字楼、酒店式公寓、厂房、库房、车位、其他...</li>
 * <li>sourceItemId: 资产来源：自管、业主放盘、大业主交管、其他...</li>
 * <li>decorateStatus: 装修状态</li>
 * <li>orientation: 朝向</li>
 * </ul>
 */
public class GetApartmentDetailResponse {
	private String buildingName;
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
	private Byte communityType;
	private String enterpriseName;
	@ItemType(OrganizationOwnerDTO.class)
	private List<OrganizationOwnerDTO> owerList;
	private Long nextPageAnchor;

	public Double getBuildArea() {
		return buildArea;
	}

	public void setBuildArea(Double buildArea) {
		this.buildArea = buildArea;
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

	public Double getRentArea() {
		return rentArea;
	}

	public void setRentArea(Double rentArea) {
		this.rentArea = rentArea;
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

	public Byte getCommunityType() {
		return communityType;
	}


	public void setCommunityType(Byte communityType) {
		this.communityType = communityType;
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


	public String getEnterpriseName() {
		return enterpriseName;
	}


	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}


	public List<OrganizationOwnerDTO> getOwerList() {
		return owerList;
	}


	public void setOwerList(List<OrganizationOwnerDTO> owerList) {
		this.owerList = owerList;
	}


	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}


	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
