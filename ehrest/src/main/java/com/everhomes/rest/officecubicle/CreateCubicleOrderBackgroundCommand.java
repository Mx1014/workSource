package com.everhomes.rest.officecubicle;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *空间后台订单
 *<li> spaceId: 空间 id	</li> 
 * <li>rentCount: 预定工位数量</li>  
 * <li>reserverName: 预订人姓名 </li> 
 * <li>reserveContactToken:  预订人联系方式</li> 
 * <li>reserveEnterpriseId: 预订人公司id</li> 
 * <li>reserveEnterpriseName:预定人公司名称</li>
 * <li>price</li>
 * <li>beginTime：预定开始时间</li>
 * <li>endTime：预定结束时间</li>
 * <li>remark：备注</li>
 * <li>ownerId</li>
 * <li>ownerType</li>
 * <li>stationId:工位Id</li>
 * <li>rentType:1长租，0短租</li>
 * </ul>
 */
public class CreateCubicleOrderBackgroundCommand {
	private Long spaceId;
    
    private String reserverName;
    private String reserverEnterpriseName;
    private Long reserverEnterpriseId;
	private String reserverContactToken;
	private BigDecimal price;
	private Long beginTime;
	private Long endTime;
	private String remark;
	private Integer rentCount;
	private Byte rentType;
	private Byte stationType;
	private String ownerType;
	private Long ownerId;
	private List<Long> stationId;
	private List<Long> roomId;
	private String userDetail;
	private Long rentalOrderNo;
	
	
	public Long getRentalOrderNo() {
		return rentalOrderNo;
	}

	public void setRentalOrderNo(Long rentalOrderNo) {
		this.rentalOrderNo = rentalOrderNo;
	}

	public String getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(String userDetail) {
		this.userDetail = userDetail;
	}

	public List<Long> getStationId() {
		return stationId;
	}

	public void setStationId(List<Long> stationId) {
		this.stationId = stationId;
	}

	public List<Long> getRoomId() {
		return roomId;
	}

	public void setRoomId(List<Long> roomId) {
		this.roomId = roomId;
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

	public Byte getStationType() {
		return stationType;
	}

	public void setStationType(Byte stationType) {
		this.stationType = stationType;
	}

	public Byte getRentType() {
		return rentType;
	}

	public void setRentType(Byte rentType) {
		this.rentType = rentType;
	}

	public Long getReserverEnterpriseId() {
		return reserverEnterpriseId;
	}

	public void setReserverEnterpriseId(Long reserverEnterpriseId) {
		this.reserverEnterpriseId = reserverEnterpriseId;
	}

	public Integer getRentCount() {
		return rentCount;
	}

	public void setRentCount(Integer rentCount) {
		this.rentCount = rentCount;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
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


	public String getReserverEnterpriseName() {
		return reserverEnterpriseName;
	}

	public void setReserverEnterpriseName(String reserverEnterpriseName) {
		this.reserverEnterpriseName = reserverEnterpriseName;
	}

	public String getReserverContactToken() {
		return reserverContactToken;
	}

	public void setReserverContactToken(String reserverContactToken) {
		this.reserverContactToken = reserverContactToken;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
