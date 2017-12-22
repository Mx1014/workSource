package com.everhomes.rest.incubator;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 *     <li>id: 标签ID</li>
 *     <li>parentId: 父ID</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>communityId: communityId</li>
 *     <li>applyUserId: applyUserId</li>
 *     <li>teamName: 团队名称</li>
 *     <li>projectType: 项目类型</li>
 *     <li>projectName: 项目名称</li>
 *     <li>businessLicenceUri: 营业执照扫描件 {[],[]}</li>
 *     <li>businessLicenceAttachments: 营业执照扫描件 {@link com.everhomes.rest.incubator.IncubatorApplyAttachmentDTO}</li>
 *     <li>planBookAttachments: 创业计划书 {@link com.everhomes.rest.incubator.IncubatorApplyAttachmentDTO}</li>
 *     <li>chargerName: 负责人名称</li>
 *     <li>chargerPhone: 负责人电话</li>
 *     <li>chargerEmail: 负责人邮件</li>
 *     <li>approveUserId: 审核人id</li>
 *     <li>approveUserName: 审核人名称</li>
 *     <li>approveStatus: 审批状态，0-待审批，1-拒绝，2-通过 参考 {@link ApproveStatus}</li>
 *     <li>approveTime: 审核时间</li>
 *     <li>approveOpinion: 审批意见</li>
 *     <li>createTime: 创建时间</li>
 *     <li>reApplyFlag: 是否允许重新申请 0-否，1-是 参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *     <li>applyType: 申请类型 0-入孵，1-加速，2-入园{@link ApplyType}</li>
 * </ul>
 */
public class IncubatorApplyDTO {

	Long id;
	Long parentId;
	Integer namespaceId;
	Long communityId;
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
	Long approveUserId;
	String approveUserName;
	Byte approveStatus;
	Timestamp approveTime;
	String approveOpinion;
	Timestamp createTime;
	Byte reApplyFlag;
	Byte applyType;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
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

	public Byte getApplyType() {
		return applyType;
	}

	public void setApplyType(Byte applyType) {
		this.applyType = applyType;
	}

	public Byte getReApplyFlag() {
		return reApplyFlag;
	}

	public void setReApplyFlag(Byte reApplyFlag) {
		this.reApplyFlag = reApplyFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
