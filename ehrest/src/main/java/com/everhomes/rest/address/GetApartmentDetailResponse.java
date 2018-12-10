// @formatter:off
package com.everhomes.rest.address;

import java.math.BigDecimal;
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
 * <li>freeArea: 可招租面积</li>
 * <li>categoryItemId: 资产类型：住宅、写字楼、酒店式公寓、厂房、库房、车位、其他...</li>
 * <li>sourceItemId: 资产来源：自管、业主放盘、大业主交管、其他...</li>
 * <li>decorateStatus: 装修状态</li>
 * <li>orientation: 朝向</li>
 * <li>apartmentFloor: 楼层名称</li>
 * <li>arrangementOperationType:房源的拆分/合并计划操作类型：拆分（0），合并（1）</li>
 * <li>relatedContractEndDate:与该房源关联的合同的结束日期</li>
 * <li>freeArea: 可招租面积</li>
 * <li>isPassiveApartment: 在参与房源合并计划时，该房源是否是被合并的房源（0：否,1：是）</li>
 * <li>reservationInvolved: 该房源是否关联预定计划（0：否,1：是）</li>
 * <li>individualCustomerInvolved: 该房源是否关联个人客户（0：否,1：是）</li>
 * <li>enterpriseCustomerInvolved: 该房源是否关联企业客户（0：否,1：是）</li>
 * <li>apartmentRent: 房源的租金</li>
 * <li>apartmentRentType: 房源的租金类型</li>
 * <li>apartAuthorizePrice: 房源授权价</li>
 * </ul>
 */
public class GetApartmentDetailResponse {
	private String buildingName;
	private Long buildingId;
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
	private Byte communityType;
	private String enterpriseName;
	@ItemType(OrganizationOwnerDTO.class)
	private List<OrganizationOwnerDTO> owerList;
	private Long nextPageAnchor;
	private Byte arrangementOperationType;
	private Long relatedContractEndDate;
	private Double freeArea;
	private Byte isPassiveApartment;
	private Byte reservationInvolved;
	private Byte arrangementInvolved;
	private Byte individualCustomerInvolved;
	private Byte enterpriseCustomerInvolved;
	private Integer buildingFloorNumber;
	private BigDecimal apartmentRent;
	private Byte apartmentRentType;
	private String apartAuthorizePrice;
	private Byte rentalContractRelated;
    
	public String getApartAuthorizePrice() {
		return apartAuthorizePrice;
	}

	public void setApartAuthorizePrice(String apartAuthorizePrice) {
		this.apartAuthorizePrice = apartAuthorizePrice;
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

	public Integer getBuildingFloorNumber() {
		return buildingFloorNumber;
	}

	public void setBuildingFloorNumber(Integer buildingFloorNumber) {
		this.buildingFloorNumber = buildingFloorNumber;
	}

	public Byte getIndividualCustomerInvolved() {
		return individualCustomerInvolved;
	}

	public void setIndividualCustomerInvolved(Byte individualCustomerInvolved) {
		this.individualCustomerInvolved = individualCustomerInvolved;
	}

	public Byte getEnterpriseCustomerInvolved() {
		return enterpriseCustomerInvolved;
	}

	public void setEnterpriseCustomerInvolved(Byte enterpriseCustomerInvolved) {
		this.enterpriseCustomerInvolved = enterpriseCustomerInvolved;
	}

	public Byte getArrangementInvolved() {
		return arrangementInvolved;
	}

	public void setArrangementInvolved(Byte arrangementInvolved) {
		this.arrangementInvolved = arrangementInvolved;
	}

	public Byte getReservationInvolved() {
		return reservationInvolved;
	}

	public void setReservationInvolved(Byte reservationInvolved) {
		this.reservationInvolved = reservationInvolved;
	}

	public Byte getIsPassiveApartment() {
		return isPassiveApartment;
	}

	public void setIsPassiveApartment(Byte isPassiveApartment) {
		this.isPassiveApartment = isPassiveApartment;
	}

	public Double getFreeArea() {
		return freeArea;
	}

	public void setFreeArea(Double freeArea) {
		this.freeArea = freeArea;
	}

	public Long getRelatedContractEndDate() {
		return relatedContractEndDate;
	}

	public void setRelatedContractEndDate(Long relatedContractEndDate) {
		this.relatedContractEndDate = relatedContractEndDate;
	}

	public Byte getArrangementOperationType() {
		return arrangementOperationType;
	}

	public void setArrangementOperationType(Byte arrangementOperationType) {
		this.arrangementOperationType = arrangementOperationType;
	}

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

	public Long getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}
	
	public Byte getRentalContractRelated() {
		return rentalContractRelated;
	}

	public void setRentalContractRelated(Byte rentalContractRelated) {
		this.rentalContractRelated = rentalContractRelated;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
