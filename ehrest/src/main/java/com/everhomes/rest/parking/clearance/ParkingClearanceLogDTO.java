// @formatter:off
package com.everhomes.rest.parking.clearance;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>flowCaseId: 工作流caseId</li>
 *     <li>applicant: 申请人</li>
 *     <li>identifierToken: 申请人手机号</li>
 *     <li>plateNumber: 车牌号</li>
 *     <li>applyTime: 申请时间</li>
 *     <li>clearanceTime: 通过时间</li>
 *     <li>remarks: 备注</li>
 *     <li>status: 状态</li>
 * </ul>
 */
public class ParkingClearanceLogDTO {

    private Long id;
    private Long flowCaseId;
    private String applicant;
    private String identifierToken;
    private String plateNumber;
    private Timestamp applyTime;
    private String applyTimeString;
    private Timestamp clearanceTime;
    private String clearanceTimeString;
    private String remarks;
    private String status;

    public String getApplyTimeString() {
		return applyTimeString;
	}

	public void setApplyTimeString(String applyTimeString) {
		this.applyTimeString = applyTimeString;
	}

	public String getClearanceTimeString() {
		return clearanceTimeString;
	}

	public void setClearanceTimeString(String clearanceTimeString) {
		this.clearanceTimeString = clearanceTimeString;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getFlowCaseId() {
        return flowCaseId;
    }

    public void setFlowCaseId(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Timestamp getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Timestamp applyTime) {
        this.applyTime = applyTime;
    }

    public Timestamp getClearanceTime() {
        return clearanceTime;
    }

    public void setClearanceTime(Timestamp clearanceTime) {
        this.clearanceTime = clearanceTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
