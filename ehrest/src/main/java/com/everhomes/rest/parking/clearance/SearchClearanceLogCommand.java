// @formatter:off
package com.everhomes.rest.parking.clearance;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>organizationId: 公司id</li>
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
public class SearchClearanceLogCommand {

    @NotNull private Long organizationId;
    @NotNull private Long communityId;
    @NotNull private Long parkingLotId;
    private Long applicantId;
    private String applicant;
    private String identifierToken;
    private String plateNumber;
    private Long startTime;
    private Long endTime;
    private Byte status;

    private Integer pageSize;
    private Long pageAnchor;

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

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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

    public String getPlateNumber() {
        return plateNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
