// @formatter:off
package com.everhomes.rest.talent;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>requestor: 发起人</li>
 * <li>phone: 电话</li>
 * <li>organizationName: 企业名称</li>
 * <li>talentId: 企业人才id</li>
 * <li>talentName: 企业人才名称</li>
 * <li>createTime: 申请时间</li>
 * <li>formItems: 表单元素，参考{@link com.everhomes.rest.general_approval.PostApprovalFormItem}</li>
 * </ul>
 */
public class TalentRequestDTO {
	private String requestor;
	private String phone;
	private String organizationName;
	private Long talentId;
	private String talentName;
	private Long createTime;
	@ItemType(FlowCaseEntity.class)
	private List<FlowCaseEntity> flowCaseEntities;

	public Long getTalentId() {
		return talentId;
	}

	public void setTalentId(Long talentId) {
		this.talentId = talentId;
	}

	public String getRequestor() {
		return requestor;
	}

	public void setRequestor(String requestor) {
		this.requestor = requestor;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getTalentName() {
		return talentName;
	}

	public void setTalentName(String talentName) {
		this.talentName = talentName;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public List<FlowCaseEntity> getFlowCaseEntities() {
		return flowCaseEntities;
	}

	public void setFlowCaseEntities(List<FlowCaseEntity> flowCaseEntities) {
		this.flowCaseEntities = flowCaseEntities;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
