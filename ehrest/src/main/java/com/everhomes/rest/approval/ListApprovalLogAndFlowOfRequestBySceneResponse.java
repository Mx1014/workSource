// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * 
 * <li>approveType: 审批类型，{@link com.everhomes.rest.approval.ApprovalType}</li>
 * <li>approvalStatus: 审批状态，参考{@link com.everhomes.rest.approval.ApprovalStatus}
 * <li>title: 申请的标题文字-根据类型不同,文字不同</li>
 * <li>approvalLogAndFlowOfRequestList: 申请的审批日志 与审批流程列表，参考{@link com.everhomes.rest.approval.ApprovalLogAndFlowOfRequestDTO}</li>
 * </ul>
 */
public class ListApprovalLogAndFlowOfRequestBySceneResponse {

	private Byte approveType;
	private Byte approvalStatus;
	private String title ;
	@ItemType(ApprovalLogAndFlowOfRequestDTO.class)
	private List<ApprovalLogAndFlowOfRequestDTO> approvalLogAndFlowOfRequestList;
	public ListApprovalLogAndFlowOfRequestBySceneResponse(){
		
	}
	
	public ListApprovalLogAndFlowOfRequestBySceneResponse(Byte approveType , Byte approvalStatus , String title ,
			List<ApprovalLogAndFlowOfRequestDTO> approvalLogAndFlowOfRequestList) {
		super();
		this.approveType = approveType;
		this.approvalStatus = approvalStatus;
		this.title = title;
		this.approvalLogAndFlowOfRequestList = approvalLogAndFlowOfRequestList;
	}

	public List<ApprovalLogAndFlowOfRequestDTO> getApprovalLogAndFlowOfRequestList() {
		return approvalLogAndFlowOfRequestList;
	}

	public void setApprovalLogAndFlowOfRequestList(List<ApprovalLogAndFlowOfRequestDTO> approvalLogAndFlowOfRequestList) {
		this.approvalLogAndFlowOfRequestList = approvalLogAndFlowOfRequestList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Byte getApproveType() {
		return approveType;
	}

	public void setApproveType(Byte approveType) {
		this.approveType = approveType;
	}

	public Byte getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Byte approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
