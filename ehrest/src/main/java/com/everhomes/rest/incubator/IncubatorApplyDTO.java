package com.everhomes.rest.incubator;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: 标签ID</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>applyUserId: applyUserId</li>
 *     <li>teamName: teamName</li>
 *     <li>projectType: projectType</li>
 *     <li>projectName: projectName</li>
 *     <li>businessLicenceUri: businessLicenceUri</li>
 *     <li>planBookUri: planBookUri</li>
 *     <li>chargerName: chargerName</li>
 *     <li>chargerPhone: chargerPhone</li>
 *     <li>chargerEmail: chargerEmail</li>
 *     <li>chargerWechat: chargerWechat</li>
 *     <li>approveUserId: approveUserId</li>
 *     <li>approveUserName: approveUserName</li>
 *     <li>approveStatus: 审批状态，0-待审批，1-拒绝，2-通过 参考 {@link ApproveStatus}</li>
 *     <li>approveTime: approveTime</li>
 *     <li>approveOpinion: approveOpinion</li>
 *     <li>createTime: createTime</li>
 * </ul>
 */
public class IncubatorApplyDTO {

	Long id;
	Integer namespaceId;
	Long applyUserId;
	String teamName;
	String projectType;
	String projectName;
	String businessLicenceUri;
	String planBookUri;
	String chargerName;
	String chargerPhone;
	String chargerEmail;
	String chargerWechat;
	Long approveUserId;
	String approveUserName;
	String approveStatus;
	Timestamp approveTime;
	String approveOpinion;
	Timestamp createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getBusinessLicenceUri() {
		return businessLicenceUri;
	}

	public void setBusinessLicenceUri(String businessLicenceUri) {
		this.businessLicenceUri = businessLicenceUri;
	}

	public String getPlanBookUri() {
		return planBookUri;
	}

	public void setPlanBookUri(String planBookUri) {
		this.planBookUri = planBookUri;
	}

	public String getChargerName() {
		return chargerName;
	}

	public void setChargerName(String chargerName) {
		this.chargerName = chargerName;
	}

	public String getChargerPhone() {
		return chargerPhone;
	}

	public void setChargerPhone(String chargerPhone) {
		this.chargerPhone = chargerPhone;
	}

	public String getChargerEmail() {
		return chargerEmail;
	}

	public void setChargerEmail(String chargerEmail) {
		this.chargerEmail = chargerEmail;
	}

	public String getChargerWechat() {
		return chargerWechat;
	}

	public void setChargerWechat(String chargerWechat) {
		this.chargerWechat = chargerWechat;
	}

	public Long getApproveUserId() {
		return approveUserId;
	}

	public void setApproveUserId(Long approveUserId) {
		this.approveUserId = approveUserId;
	}

	public String getApproveUserName() {
		return approveUserName;
	}

	public void setApproveUserName(String approveUserName) {
		this.approveUserName = approveUserName;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public Timestamp getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(Timestamp approveTime) {
		this.approveTime = approveTime;
	}

	public String getApproveOpinion() {
		return approveOpinion;
	}

	public void setApproveOpinion(String approveOpinion) {
		this.approveOpinion = approveOpinion;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getApplyUserId() {
		return applyUserId;
	}

	public void setApplyUserId(Long applyUserId) {
		this.applyUserId = applyUserId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
