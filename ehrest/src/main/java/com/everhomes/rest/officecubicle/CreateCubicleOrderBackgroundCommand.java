package com.everhomes.rest.officecubicle;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *空间订单
 *<li> siteId: office site id	</li> 
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
	private Long stationId;

	
	
	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
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
