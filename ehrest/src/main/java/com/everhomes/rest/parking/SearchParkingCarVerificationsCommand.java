package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>ownerType : 目前是community 参考 {@link ParkingOwnerType}</li>
 * <li>ownerId : 目前是小区/园区ID</li>
 * <li>parkingLotId : 停车场id</li>
 * <li>plateNumber: 车牌</li>
 * <li>plateOwnerName: 车主名称</li>
 * <li>plateOwnerPhone: 车数手机号</li>
 * <li>ownerKeyWords: 车主关键字(名称或者手机号) V6.6</li>
 * <li>requestorEnterpriseName: 申请人公司名称</li>
 * <li>startTime: 开始时间</li>
 * <li>endTime: 结束时间</li>
 * <li>status: status {@link ParkingCarVerificationStatus}</li>
 * <li>pageAnchor: pageAnchor</li>
 * <li>pageSize: pageSize</li>
 * <li>currentPMId: 当前管理公司ID</li>
 * <li>currentProjectId: 当前选中项目Id，如果是全部则不传</li>
 * <li>appId: 应用id</li>
 * </ul>
 */
public class SearchParkingCarVerificationsCommand {

    @NotNull
    private String ownerType;
    @NotNull
    private Long ownerId;
    @NotNull
    private Long parkingLotId;

    private String plateNumber;
    private String plateOwnerName;
    private String plateOwnerPhone;
    private String ownerKeyWords;
    private String requestorEnterpriseName;

    private Long startTime;
    private Long endTime;
    private Byte status;

    private Long pageAnchor;
    private Integer pageSize;
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

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
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

    public String getRequestorEnterpriseName() {
        return requestorEnterpriseName;
    }

    public void setRequestorEnterpriseName(String requestorEnterpriseName) {
        this.requestorEnterpriseName = requestorEnterpriseName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
