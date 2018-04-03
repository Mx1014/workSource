package com.everhomes.parking.clearance;

import com.everhomes.util.StringHelper;

/**
 * <p>车辆放行记录查询对象，封装了查询条件</p>
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 小区id</li>
 *     <li>ownerType: 停车场id</li>
 *     <li>applicantId: 申请人id</li>
 *     <li>applicant: 申请人</li>
 *     <li>identifierToken: 手机号</li>
 *     <li>plateNumber: 车牌号</li>
 *     <li>startTime: 开始时间, 时间戳</li>
 *     <li>endTime: 结束时间, 时间戳</li>
 *     <li>status: 状态 {@link com.everhomes.rest.parking.clearance.ParkingClearanceLogStatus}</li>
 *     <li>pageSize: 一页条数</li>
 *     <li>pageAnchor: 下一页锚点</li>
 * </ul>
 */
public class ParkingClearanceLogQueryObject {

    private Integer namespaceId;
    private Long communityId;
    private Long parkingLotId;
    private Long applicantId;
    private String applicant;
    private String identifierToken;
    private String plateNumber;
    private Long startTime;
    private Long endTime;
    private Byte status;

    private Integer pageSize;
    private Long pageAnchor;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public Long getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(Long applicantId) {
        this.applicantId = applicantId;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getIdentifierToken() {
        return identifierToken;
    }

    public void setIdentifierToken(String identifierToken) {
        this.identifierToken = identifierToken;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
