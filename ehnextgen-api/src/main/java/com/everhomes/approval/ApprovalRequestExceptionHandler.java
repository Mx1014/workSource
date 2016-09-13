package com.everhomes.approval;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.rest.approval.ApprovalBasicInfoOfRequestDTO;
import com.everhomes.rest.approval.ApprovalTypeTemplateCode;
import com.everhomes.rest.approval.ExceptionRequestBasicDescription;

@Component(ApprovalRequestHandler.APPROVAL_REQUEST_OBJECT_PREFIX + ApprovalTypeTemplateCode.EXCEPTION)
public class ApprovalRequestExceptionHandler extends ApprovalRequestDefaultHandler {

	@Override
	public ApprovalBasicInfoOfRequestDTO processApprovalBasicInfoOfRequest(ApprovalRequest approvalRequest) {
		ApprovalBasicInfoOfRequestDTO approvalBasicInfo = super.processApprovalBasicInfoOfRequest(approvalRequest);
		ExceptionRequestBasicDescription description = new ExceptionRequestBasicDescription();
		JSONObject contentJsonObject = JSONObject.parseObject(approvalRequest.getContentJson());
		description.setPunchDate(Timestamp.valueOf(contentJsonObject.getString("punchDate")));
		description.setPunchDetail(contentJsonObject.getString("punchDetail"));
		description.setPunchStatusName(contentJsonObject.getString("punchStatusName"));
		
		approvalBasicInfo.setDescriptionJson(JSON.toJSONString(description));
		return approvalBasicInfo;
	}
	
}
