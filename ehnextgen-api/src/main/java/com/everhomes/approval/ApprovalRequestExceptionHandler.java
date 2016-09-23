package com.everhomes.approval;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.news.AttachmentProvider;
import com.everhomes.rest.approval.ApprovalBasicInfoOfRequestDTO;
import com.everhomes.rest.approval.ApprovalExceptionContent;
import com.everhomes.rest.approval.ApprovalNotificationTemplateCode;
import com.everhomes.rest.approval.ApprovalOwnerInfo;
import com.everhomes.rest.approval.ApprovalServiceErrorCode;
import com.everhomes.rest.approval.ApprovalStatus;
import com.everhomes.rest.approval.ApprovalTypeTemplateCode;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.CreateApprovalRequestBySceneCommand;
import com.everhomes.rest.approval.ExceptionRequestBasicDescription;
import com.everhomes.rest.approval.ExceptionRequestDTO;
import com.everhomes.rest.approval.ExceptionRequestType;
import com.everhomes.rest.techpark.punch.PunchRquestType;
import com.everhomes.rest.techpark.punch.PunchStatus;
import com.everhomes.rest.techpark.punch.PunchTimesPerDay;
import com.everhomes.rest.techpark.punch.ViewFlags;
import com.everhomes.server.schema.tables.pojos.EhApprovalAttachments;
import com.everhomes.techpark.punch.PunchExceptionApproval;
import com.everhomes.techpark.punch.PunchExceptionRequest;
import com.everhomes.techpark.punch.PunchProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

/**
 * 
 * <ul>
 * 打卡异常申请handler
 * </ul>
 */
@Component(ApprovalRequestHandler.APPROVAL_REQUEST_OBJECT_PREFIX + ApprovalTypeTemplateCode.EXCEPTION)
public class ApprovalRequestExceptionHandler extends ApprovalRequestDefaultHandler {

	private static final SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	private PunchProvider punchProvider;
	
	@Autowired
	private AttachmentProvider attachmentProvider;
	
	@Autowired
	private ApprovalService approvalService;

	@Autowired
	private LocaleTemplateService localeTemplateService;
	
	@Override
	public ApprovalBasicInfoOfRequestDTO processApprovalBasicInfoOfRequest(ApprovalRequest approvalRequest) {
		ApprovalBasicInfoOfRequestDTO approvalBasicInfo = super.processApprovalBasicInfoOfRequest(approvalRequest);
		ExceptionRequestBasicDescription description = new ExceptionRequestBasicDescription();
		ApprovalExceptionContent content = JSONObject.parseObject(approvalRequest.getContentJson(), ApprovalExceptionContent.class);
		description.setPunchDate(new Timestamp(content.getPunchDate()));
		description.setExceptionRequestType(content.getExceptionRequestType());
		description.setPunchDetail(content.getPunchDetail());
		description.setPunchStatusName(content.getPunchStatusName());
		
		approvalBasicInfo.setDescriptionJson(JSON.toJSONString(description));
		return approvalBasicInfo;
	}

	@Override
	public ApprovalRequest preProcessCreateApprovalRequest(Long userId, ApprovalOwnerInfo ownerInfo,
			CreateApprovalRequestBySceneCommand cmd) {
		if (StringUtils.isBlank(cmd.getReason())) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE, ApprovalServiceErrorCode.EXCEPTION_EMPTY_REASON,
					"reason cannot be empty");
		}
		ApprovalExceptionContent approvalExceptionContent = JSONObject.parseObject(cmd.getContentJson(), ApprovalExceptionContent.class);
		if (approvalExceptionContent.getPunchDate() == null || ExceptionRequestType.fromCode(approvalExceptionContent.getExceptionRequestType()) == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"invalid parameters, content json="+approvalExceptionContent);
		}
		try {
			approvalExceptionContent.setPunchDate(dateSF.parse(dateSF.format(new Date(approvalExceptionContent.getPunchDate()))).getTime());
		} catch (ParseException e) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"format punch date error");
		}
		
		ApprovalRequest approvalRequest = super.preProcessCreateApprovalRequest(userId, ownerInfo, cmd);
		approvalRequest.setContentJson(JSON.toJSONString(approvalExceptionContent));
		approvalRequest.setLongTag1(approvalExceptionContent.getPunchDate());
		
		PunchExceptionRequest punchExceptionRequest = getPunchExceptionRequest(userId, ownerInfo.getOwnerId(), approvalExceptionContent.getPunchDate(), approvalExceptionContent.getExceptionRequestType()); 
		if (punchExceptionRequest != null) {
			approvalRequest.setId(punchExceptionRequest.getRequestId());
			//因为异常申请，再次申请时是同一张单据，所以需要删除之前申请的附件
			attachmentProvider.deleteAttachmentByOwnerId(EhApprovalAttachments.class, approvalRequest.getId());
		}
		
		return approvalRequest;
	}

	@Override
	public void postProcessCreateApprovalRequest(Long userId, ApprovalOwnerInfo ownerInfo, ApprovalRequest approvalRequest,
			CreateApprovalRequestBySceneCommand cmd) {
		ApprovalExceptionContent approvalExceptionContent = JSONObject.parseObject(cmd.getContentJson(), ApprovalExceptionContent.class);
		//处理考勤
		PunchExceptionRequest punchExceptionRequest = getPunchExceptionRequest(userId, ownerInfo.getOwnerId(), approvalExceptionContent.getPunchDate(), approvalExceptionContent.getExceptionRequestType()); 
		if (punchExceptionRequest != null) {
			punchExceptionRequest.setDescription(approvalRequest.getReason());
			punchProvider.updatePunchExceptionRequest(punchExceptionRequest);
		} else {
			createPunchExceptionRequest(userId, ownerInfo, approvalRequest, approvalExceptionContent);
		}
	}

	private void createPunchExceptionRequest(Long userId, ApprovalOwnerInfo ownerInfo, ApprovalRequest approvalRequest,
			ApprovalExceptionContent approvalExceptionContent) {
		PunchExceptionRequest punchExceptionRequest = new PunchExceptionRequest();
		punchExceptionRequest.setUserId(userId);
		punchExceptionRequest.setEnterpriseId(ownerInfo.getOwnerId());
		punchExceptionRequest.setPunchDate(new Date(approvalExceptionContent.getPunchDate()));
		punchExceptionRequest.setRequestType(PunchRquestType.REQUEST.getCode());
		punchExceptionRequest.setDescription(approvalRequest.getReason());
		punchExceptionRequest.setStatus(CommonStatus.ACTIVE.getCode());
		punchExceptionRequest.setCreatorUid(userId);
		punchExceptionRequest.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		punchExceptionRequest.setOperateTime(punchExceptionRequest.getCreateTime());
		punchExceptionRequest.setOperatorUid(userId);
		punchExceptionRequest.setViewFlag(ViewFlags.NOTVIEW.getCode());
		punchExceptionRequest.setRequestId(approvalRequest.getId());
		if (approvalExceptionContent.getExceptionRequestType().byteValue() == ExceptionRequestType.ALL_DAY.getCode()) {
			punchExceptionRequest.setApprovalStatus(PunchStatus.NORMAL.getCode());
		}else if (approvalExceptionContent.getExceptionRequestType().byteValue() == ExceptionRequestType.MORNING.getCode()) {
				punchExceptionRequest.setMorningApprovalStatus(PunchStatus.NORMAL.getCode());
		}else if (approvalExceptionContent.getExceptionRequestType().byteValue() == ExceptionRequestType.AFTERNOON.getCode()) {
				punchExceptionRequest.setAfternoonApprovalStatus(PunchStatus.NORMAL.getCode());
		}
		punchProvider.createPunchExceptionRequest(punchExceptionRequest);
	}
	
	private PunchExceptionRequest getPunchExceptionRequest(Long userId, Long ownerId, Long punchDate, Byte exceptionRequestType){
		//异常申请的重新申请是同一张单据，所以需要检查一下是否之前申请过，如果申请过则更新之前的
		return punchProvider.findPunchExceptionRequest(userId, ownerId, punchDate, exceptionRequestType);
	}

	@Override
	public void processCancelApprovalRequest(ApprovalRequest approvalRequest) {
		PunchExceptionRequest punchExceptionRequest = punchProvider.findPunchExceptionRequestByRequestId(approvalRequest.getOwnerId(), approvalRequest.getCreatorUid(), approvalRequest.getId());
		if (punchExceptionRequest != null) {
			//为保证以前的接口正常，这里直接删除记录而不是更新状态
			punchProvider.deletePunchExceptionRequest(punchExceptionRequest);
		}
	}

	@Override
	public void processFinalApprove(ApprovalRequest approvalRequest) {
		// 更新或插入一条记录到eh_punch_exception_approvals中
		ApprovalExceptionContent approvalExceptionContent = JSONObject.parseObject(approvalRequest.getContentJson(), ApprovalExceptionContent.class);
		PunchExceptionApproval punchExceptionApproval = punchProvider.findPunchExceptionApproval(approvalRequest.getCreatorUid(), approvalRequest.getOwnerId(), new Date(approvalExceptionContent.getPunchDate()));
		if (punchExceptionApproval != null) {
			if (approvalExceptionContent.getExceptionRequestType().byteValue() == ExceptionRequestType.ALL_DAY.getCode()) {
				punchExceptionApproval.setApprovalStatus(PunchStatus.NORMAL.getCode());
			}else if (approvalExceptionContent.getExceptionRequestType().byteValue() == ExceptionRequestType.MORNING.getCode()) {
				punchExceptionApproval.setMorningApprovalStatus(PunchStatus.NORMAL.getCode());
			}else if (approvalExceptionContent.getExceptionRequestType().byteValue() == ExceptionRequestType.AFTERNOON.getCode()) {
				punchExceptionApproval.setAfternoonApprovalStatus(PunchStatus.NORMAL.getCode());
			}
			punchExceptionApproval.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			punchExceptionApproval.setOperatorUid(UserContext.current().getUser().getId());
			punchProvider.updatePunchExceptionApproval(punchExceptionApproval);
		}else {
			punchExceptionApproval = new PunchExceptionApproval();
			punchExceptionApproval.setUserId(approvalRequest.getCreatorUid());
			punchExceptionApproval.setEnterpriseId(approvalRequest.getOwnerId());
			punchExceptionApproval.setPunchDate(new Date(approvalExceptionContent.getPunchDate()));
			punchExceptionApproval.setCreatorUid(UserContext.current().getUser().getId());
			punchExceptionApproval.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			punchExceptionApproval.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			punchExceptionApproval.setOperatorUid(UserContext.current().getUser().getId());
			punchExceptionApproval.setViewFlag(ViewFlags.NOTVIEW.getCode());
			if (approvalExceptionContent.getExceptionRequestType().byteValue() == ExceptionRequestType.ALL_DAY.getCode()) {
				punchExceptionApproval.setApprovalStatus(PunchStatus.NORMAL.getCode());
				punchExceptionApproval.setPunchTimesPerDay(PunchTimesPerDay.TWICE.getCode());
			}else if (approvalExceptionContent.getExceptionRequestType().byteValue() == ExceptionRequestType.MORNING.getCode()) {
				punchExceptionApproval.setMorningApprovalStatus(PunchStatus.NORMAL.getCode());
				punchExceptionApproval.setPunchTimesPerDay(PunchTimesPerDay.FORTH.getCode());
			}else if (approvalExceptionContent.getExceptionRequestType().byteValue() == ExceptionRequestType.AFTERNOON.getCode()) {
				punchExceptionApproval.setAfternoonApprovalStatus(PunchStatus.NORMAL.getCode());
				punchExceptionApproval.setPunchTimesPerDay(PunchTimesPerDay.FORTH.getCode());
			}
			punchProvider.createPunchExceptionApproval(punchExceptionApproval);
		}
	}

	@Override
	public String processListApprovalRequest(List<ApprovalRequest> approvalRequestList) {
		List<ExceptionRequestDTO> resultList = approvalRequestList.stream().map(a->{
			ExceptionRequestDTO exceptionRequest = new ExceptionRequestDTO();
			ApprovalExceptionContent approvalExceptionContent = JSONObject.parseObject(a.getContentJson(), ApprovalExceptionContent.class);
			exceptionRequest.setRequestId(a.getId());
			exceptionRequest.setPunchDate(new Timestamp(approvalExceptionContent.getPunchDate()));
			exceptionRequest.setExceptionRequestType(approvalExceptionContent.getExceptionRequestType());
			exceptionRequest.setNickName(approvalService.getUserName(a.getCreatorUid(), a.getOwnerId()));
			exceptionRequest.setReason(a.getReason());
			exceptionRequest.setPunchStatusName(approvalExceptionContent.getPunchStatusName());
			return exceptionRequest;
		}).collect(Collectors.toList());
		
		
		return JSON.toJSONString(resultList);
	}

	@Override
	public String processMessageToCreatorBody(ApprovalRequest approvalRequest, String reason) {
		String scope = null;
		int code = 0;
		Map<String, Object> map = new HashMap<>();
		map.put("punchDate", getPunchDate(approvalRequest));
		if (approvalRequest.getApprovalStatus().byteValue() == ApprovalStatus.AGREEMENT.getCode()) {
			scope = ApprovalNotificationTemplateCode.SCOPE;
			code = ApprovalNotificationTemplateCode.EXCEPTION_APPROVED;
		}else {
			scope = ApprovalNotificationTemplateCode.SCOPE;
			code = ApprovalNotificationTemplateCode.EXCEPTION_REJECTED;
			map.put("reason", StringUtils.isBlank(reason)?approvalRequest.getReason():reason);
			map.put("approver", approvalService.getUserName(approvalRequest.getOperatorUid(), approvalRequest.getOwnerId()));
		}
		return localeTemplateService.getLocaleTemplateString(scope, code, UserContext.current().getUser().getLocale(), map, "");
	}

	private String getPunchDate(ApprovalRequest approvalRequest) {
		SimpleDateFormat dateSF = new SimpleDateFormat("MM月dd日");
		ApprovalExceptionContent content = JSONObject.parseObject(approvalRequest.getContentJson(), ApprovalExceptionContent.class);
		String result = dateSF.format(new java.util.Date(content.getPunchDate()));
		if (result.startsWith("0")) {
			return result.substring(1);
		}
		return result;
	}

	@Override
	public String processMessageToNextLevelBody(ApprovalRequest approvalRequest) {
		String scope = null;
		int code = 0;
		Map<String, Object> map = new HashMap<>();
		map.put("creatorName", approvalService.getUserName(approvalRequest.getCreatorUid(), approvalRequest.getOwnerId()));
		//当前级别为0表示用户刚提交
		if (approvalRequest.getCurrentLevel().byteValue() == (byte)0) {
			scope = ApprovalNotificationTemplateCode.SCOPE;
			code = ApprovalNotificationTemplateCode.EXCEPTION_COMMIT_REQUEST;
		}else {
			scope = ApprovalNotificationTemplateCode.SCOPE;
			code = ApprovalNotificationTemplateCode.EXCEPTION_TO_NEXT_LEVEL;
			map.put("approver", approvalService.getUserName(approvalRequest.getOperatorUid(), approvalRequest.getOwnerId()));
		}
		return localeTemplateService.getLocaleTemplateString(scope, code, UserContext.current().getUser().getLocale(), map, "");
	}
	
}
