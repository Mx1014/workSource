package com.everhomes.rest.incubator;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 *     <li>id: 标签ID</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>applyUserId: applyUserId</li>
 *     <li>teamName: teamName</li>
 *     <li>projectType: projectType</li>
 *     <li>projectName: projectName</li>
 *     <li>businessLicenceUri: {[],[]}</li>
 *     <li>businessLicenceAttachments: businessLicenceAttachments {@link com.everhomes.rest.incubator.IncubatorApplyAttachmentDTO}</li>
 *     <li>planBookAttachments: planBookAttachments {@link com.everhomes.rest.incubator.IncubatorApplyAttachmentDTO}</li>
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
 *     <li>reApplyId: 如果该记录被拒绝后又重新申请，此字段为新申请记录的Id</li>
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
	@ItemType(IncubatorApplyAttachmentDTO.class)
	List<IncubatorApplyAttachmentDTO> businessLicenceAttachments;
	@ItemType(IncubatorApplyAttachmentDTO.class)
	List<IncubatorApplyAttachmentDTO> planBookAttachments;
	String chargerName;
	String chargerPhone;
	String chargerEmail;
	String chargerWechat;
	Long approveUserId;
	String approveUserName;
	Byte approveStatus;
	Timestamp approveTime;
	String approveOpinion;
	Timestamp createTime;
	Long reApplyId;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<IncubatorApplyAttachmentDTO> getBusinessLicenceAttachments() {
		return businessLicenceAttachments;
	}

	public void setBusinessLicenceAttachments(List<IncubatorApplyAttachmentDTO> businessLicenceAttachments) {
		this.businessLicenceAttachments = businessLicenceAttachments;
	}

	public List<IncubatorApplyAttachmentDTO> getPlanBookAttachments() {
		return planBookAttachments;
	}

	public void setPlanBookAttachments(List<IncubatorApplyAttachmentDTO> planBookAttachments) {
		this.planBookAttachments = planBookAttachments;
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

	public Byte getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(Byte approveStatus) {
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

	public Long getReApplyId() {
		return reApplyId;
	}

	public void setReApplyId(Long reApplyId) {
		this.reApplyId = reApplyId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
