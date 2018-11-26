package com.everhomes.rest.officecubicle;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *空间订单
 *<li> siteId: office site id	</li> 
 * <li>orderType: 预定类别：1-参观 2-预定 </li>  
 * <li>rentType: 租赁类别:1-开放式（默认space_type 1）,2-办公室 {@link com.everhomes.rest.officecubicle.OfficeRentType}</li>  
 * <li>positionNums: 工位数量</li>  
 * <li>reserverName: 预订人姓名 </li> 
 * <li>reserveContactToken:  预订人联系方式</li> 
 * <li>reserveEnterprise: 预订人公司</li> 
 * <li>categoryName: 预定空间名称</li>
 * <li>categoryId: 空间id</li>
 * <li>price</li>
 * <li>beginTime：预定开始时间</li>
 * <li>endTime：预定结束时间</li>
 * <li>remark：备注</li>
 * <li>cubicleType:工位性质 0-短租，1-长租</li>
 * <li>requestType: 请求来源，为空或者0:客户端 1:后台管理 {@link com.everhomes.rest.officeCubicle.OfficeCubicleRequestType}</li>
 * </ul>
 */
public class CreateOfficeCubicleOrderCommand {

	private Long spaceId;
    
    private Integer rentCount;
    private String reserverName;
    private String reserveEnterprise;
	private String reserveContactToken;
	private String remark;
	private Long rentalOrderNo;
	private Byte rentType;
	private Byte stationType;
	private String ownerType;
	private Long ownerId;
	private Long stationId;
	private Byte paymentType;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	  


	public Byte getPaymentType() {
		return paymentType;
	}



	public void setPaymentType(Byte paymentType) {
		this.paymentType = paymentType;
	}



	public Long getSpaceId() {
		return spaceId;
	}


	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}


	public String getReserverName() {
		return reserverName;
	}


	public void setReserverName(String reserverName) {
		this.reserverName = reserverName;
	}


	public String getReserveEnterprise() {
		return reserveEnterprise;
	}


	public void setReserveEnterprise(String reserveEnterprise) {
		this.reserveEnterprise = reserveEnterprise;
	}


	public String getReserveContactToken() {
		return reserveContactToken;
	}


	public void setReserveContactToken(String reserveContactToken) {
		this.reserveContactToken = reserveContactToken;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getRentalOrderNo() {
		return rentalOrderNo;
	}

	public void setRentalOrderNo(Long rentalOrderNo) {
		this.rentalOrderNo = rentalOrderNo;
	}



	public Integer getRentCount() {
		return rentCount;
	}



	public void setRentCount(Integer rentCount) {
		this.rentCount = rentCount;
	}



	public Byte getRentType() {
		return rentType;
	}



	public void setRentType(Byte rentType) {
		this.rentType = rentType;
	}



	public Byte getStationType() {
		return stationType;
	}



	public void setStationType(Byte stationType) {
		this.stationType = stationType;
	}



	public String getOwnerType() {
		return ownerType;
	}



	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}



	public Long getOwnerId() {
		return ownerId;
	}



	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}



	public Long getStationId() {
		return stationId;
	}



	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	
}
