package com.everhomes.approval;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.approval.ApprovalBasicInfoOfRequestDTO;
import com.everhomes.rest.approval.ApprovalExceptionContent;
import com.everhomes.rest.approval.ApprovalOwnerInfo;
import com.everhomes.rest.approval.ApprovalTypeTemplateCode;
import com.everhomes.rest.approval.CreateApprovalRequestBySceneCommand;
import com.everhomes.rest.approval.ExceptionRequestBasicDescription;
import com.everhomes.util.RuntimeErrorException;

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

	@Override
	public ApprovalRequest preProcessCreateApprovalRequest(Long userId, ApprovalOwnerInfo ownerInfo,
			CreateApprovalRequestBySceneCommand cmd) {
		ApprovalRequest approvalRequest = super.preProcessCreateApprovalRequest(userId, ownerInfo, cmd);
		if (StringUtils.isBlank(cmd.getReason())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"reason cannot be empty");
		}
		ApprovalExceptionContent approvalExceptionContent = JSONObject.parseObject(cmd.getContentJson(), ApprovalExceptionContent.class);
		approvalRequest.setContentJson(JSON.toJSONString(approvalExceptionContent));
		
		//异常申请的重新申请是同一张单据，所以需要检查一下是否之前申请过，如果申请过则更新之前的
		
		
		return approvalRequest;
	}
	
}
