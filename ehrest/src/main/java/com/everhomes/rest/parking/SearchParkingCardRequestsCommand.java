// @formatter:off
package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>parkingLotId: 停车场ID</li>
 * <li>plateNumber: 车牌号</li>
 * <li>plateOwnerName: 车主名称</li>
 * <li>plateOwnerPhone: 车主手机号</li>
 * <li>ownerKeyWords: 车主关键字(名称或者手机号) V6.6</li>
 * <li>plateOwnerEntperiseName: plateOwnerEntperiseName</li>
 * <li>startDate: 开始时间</li>
 * <li>endDate: 结束时间</li>
 * <li>status: 申请状态，{@link com.everhomes.rest.parking.ParkingCardRequestStatus}</li>
 * <li>pageAnchor: 本页开始的锚点</li>
 * <li>pageSize: 每页的数量</li>
 * <li>flowId: flowId</li>
 * <li>carBrand: 车品牌</li>
 * <li>carSerieName: 车系</li>
 * <li>cardTypeId: 卡类型id</li>
 * <li>currentPMId: 当前管理公司ID</li>
 * <li>currentProjectId: 当前选中项目Id，如果是全部则不传</li>
 * <li>appId: 应用id</li>
 * </ul>
 */
public class SearchParkingCardRequestsCommand {
    private String ownerType;
    private Long ownerId;
    private Long parkingLotId;
    private String plateNumber;
    private String plateOwnerName;
    private String plateOwnerPhone;
    private String ownerKeyWords;
    private String plateOwnerEntperiseName;
    private Long startDate;
    private Long endDate;
    private Byte status;
    private Long pageAnchor;
    private Integer pageSize;

    private Long flowId;
    private String carBrand;
    private String carSerieName;

    private String cardTypeId;
	private Long currentPMId;
	private Long currentProjectId;
	private Long appId;
	private Integer pageNum;
	
	

    public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public String getOwnerKeyWords() {
        return ownerKeyWords;
    }

    public void setOwnerKeyWords(String ownerKeyWords) {
        this.ownerKeyWords = ownerKeyWords;
    }

    public Long getCurrentPMId() {
		return currentPMId;
	}

	public void setCurrentPMId(Long currentPMId) {
		this.currentPMId = currentPMId;
	}

	public Long getCurrentProjectId() {
		return currentProjectId;
	}

	public void setCurrentProjectId(Long currentProjectId) {
		this.currentProjectId = currentProjectId;
	}
	
	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

    public String getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(String cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    public SearchParkingCardRequestsCommand() {
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

    public Long getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getPlateOwnerName() {
        return plateOwnerName;
    }

    public void setPlateOwnerName(String plateOwnerName) {
        this.plateOwnerName = plateOwnerName;
    }

    public String getPlateOwnerPhone() {
        return plateOwnerPhone;
    }

    public void setPlateOwnerPhone(String plateOwnerPhone) {
        this.plateOwnerPhone = plateOwnerPhone;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarSerieName() {
        return carSerieName;
    }

    public void setCarSerieName(String carSerieName) {
        this.carSerieName = carSerieName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public String getPlateOwnerEntperiseName() {
        return plateOwnerEntperiseName;
    }

    public void setPlateOwnerEntperiseName(String plateOwnerEntperiseName) {
        this.plateOwnerEntperiseName = plateOwnerEntperiseName;
    }
}
