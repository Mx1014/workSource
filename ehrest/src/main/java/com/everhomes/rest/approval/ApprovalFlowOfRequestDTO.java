package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>currentFlag: 是否为当前节点，参考{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * <li>nickNameList: 审批人姓名列表</li>
 * </ul>
 */
public class ApprovalFlowOfRequestDTO {
	private Byte currentFlag;
	private List<String> nickNameList;

	public Byte getCurrentFlag() {
		return currentFlag;
	}

	public void setCurrentFlag(Byte currentFlag) {
		this.currentFlag = currentFlag;
	}

	public List<String> getNickNameList() {
		return nickNameList;
	}

	public void setNickNameList(List<String> nickNameList) {
		this.nickNameList = nickNameList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}}
