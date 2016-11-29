package com.everhomes.approval;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.news.AttachmentProvider;
import com.everhomes.rest.approval.ApprovalBasicInfoOfRequestDTO;
import com.everhomes.rest.approval.ApprovalLogTitleTemplateCode;
import com.everhomes.rest.approval.ApprovalNotificationTemplateCode;
import com.everhomes.rest.approval.ApprovalOwnerInfo;
import com.everhomes.rest.approval.ApprovalServiceErrorCode;
import com.everhomes.rest.approval.ApprovalStatus;
import com.everhomes.rest.approval.ApprovalTypeTemplateCode;
import com.everhomes.rest.approval.BasicDescriptionDTO;
import com.everhomes.rest.approval.BriefApprovalRequestDTO;
import com.everhomes.rest.approval.CreateApprovalRequestBySceneCommand;
import com.everhomes.rest.approval.RequestDTO;
import com.everhomes.rest.techpark.punch.PunchTimesPerDay;
import com.everhomes.techpark.punch.PunchDayLog;
import com.everhomes.techpark.punch.PunchProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;

/**
 * 
 * <ul>
 * 打卡加班申请handler
 * </ul>
 */
@Component(ApprovalRequestHandler.APPROVAL_REQUEST_OBJECT_PREFIX + ApprovalTypeTemplateCode.OVERTIME)
public class ApprovalRequestOvertimeHandler extends ApprovalRequestDefaultHandler {

	private static final SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");



	@Autowired
	private ApprovalOpRequestProvider approvalOpRequestProvider;
	
	@Autowired
	private PunchProvider punchProvider;
	
	@Autowired
	private AttachmentProvider attachmentProvider;
	
	@Autowired
	private ApprovalService approvalService;

	@Autowired
	private LocaleTemplateService localeTemplateService;

	@Autowired
	private ApprovalRequestProvider approvalRequestProvider;

	@Autowired
	private ApprovalRuleProvider approvalRuleProvider;

	@Autowired
	LocaleStringService localeStringService;
	
	@Override
	public ApprovalRequest preProcessCreateApprovalRequest(Long userId, ApprovalOwnerInfo ownerInfo,
			CreateApprovalRequestBySceneCommand cmd) {
		if (StringUtils.isBlank(cmd.getReason())) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE, ApprovalServiceErrorCode.EXCEPTION_EMPTY_REASON,
					"reason cannot be empty");
		} 
		if (cmd.getEffectiveDate() == null || cmd.getHourLength() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"invalid parameters, EffectiveDate or  HourLength cannot be null" );
		}  

		List<ApprovalRequest> approvalRequests =  this.approvalRequestProvider.listApprovalRequestByEffectiveDateAndCreateUid(ownerInfo.getNamespaceId(),
				ownerInfo.getOwnerType(), ownerInfo.getOwnerId(), cmd.getApprovalType(), new Date(cmd.getEffectiveDate()), userId, null);
		//创建
		ApprovalRequest approvalRequest = super.preProcessCreateApprovalRequest(userId, ownerInfo, cmd);
		if (approvalRequests != null && approvalRequests.size()>0) {
			for (ApprovalRequest request : approvalRequests){
				//删除之前的request
				this.approvalRequestProvider.deleteApprovalRequest(request);
				//删掉之前的审批流程
				approvalOpRequestProvider.deleteApprovalOpRequestByRequestId(request.getId());
			}
		}
		 	
		return approvalRequest;
	}
	 
	@Override
	public List<RequestDTO> processListApprovalRequest(List<ApprovalRequest> approvalRequestList) {
		List<RequestDTO> resultList = approvalRequestList.stream().map(a->{
			RequestDTO exceptionRequest = new RequestDTO();
			
			exceptionRequest.setRequestId(a.getId());  
			exceptionRequest.setNickName(approvalService.getUserName(a.getCreatorUid(), a.getOwnerId()));
			exceptionRequest.setReason(a.getReason()); 
			exceptionRequest.setRequestInfo(processRequestDate(a.getEffectiveDate())+
					localeStringProvider.find(ApprovalTypeTemplateCode.SCOPE, a.getApprovalType().toString(), UserContext.current()
							.getUser().getLocale()).getText()+ a.getHourLength()+"小时");
			PunchDayLog pdl = this.punchProvider.getDayPunchLogByDate(a.getCreatorUid(), a.getOwnerId(), dateSF.format(a.getEffectiveDate()));
			
			String punchDetail = processPunchDetail(pdl);

			exceptionRequest.setPunchDetail(punchDetail);
			
			return exceptionRequest;
		}).collect(Collectors.toList());
		
		
		return resultList;
	}

	@Override
	public ApprovalBasicInfoOfRequestDTO processApprovalBasicInfoOfRequest(ApprovalRequest a) {
		ApprovalBasicInfoOfRequestDTO approvalBasicInfo = super.processApprovalBasicInfoOfRequest(a);
		PunchDayLog pdl = this.punchProvider.getDayPunchLogByDate(a.getCreatorUid(), a.getOwnerId(), dateSF.format(a.getEffectiveDate()));
		BasicDescriptionDTO dto = new BasicDescriptionDTO();
		dto.setPunchDate(new Timestamp(a.getEffectiveDate().getTime()));
		dto.setHourLength(a.getHourLength());
		dto.setPunchDetail(processPunchDetail(pdl));
		approvalBasicInfo.setDescriptionJson(dto.toString());
		return approvalBasicInfo;
	}

	@Override
	public BriefApprovalRequestDTO processBriefApprovalRequest(ApprovalRequest approvalRequest) {
		BriefApprovalRequestDTO briefApprovalRequestDTO = super.processBriefApprovalRequest(approvalRequest);
		  
		briefApprovalRequestDTO.setTitle(processBriefRequestTitle(approvalRequest  ));
		return briefApprovalRequestDTO;
	}
	private String processBriefRequestTitle(ApprovalRequest a  ) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		 
		// 初次提交
		String scope = ApprovalLogTitleTemplateCode.SCOPE;
		int code = ApprovalLogTitleTemplateCode.OVERTIME_TITLE;
		map.put("nickName",approvalService.getUserName(a.getCreatorUid(), a.getOwnerId()) );
		map.put("date",processRequestDate(a.getEffectiveDate()));
		map.put("hour",a.getHourLength());
		 
		String result = localeTemplateService.getLocaleTemplateString(scope, code, UserContext.current().getUser().getLocale(), map, "");
		
		return result;
	}

	@Override
	public String processMessageToCreatorBody(ApprovalRequest approvalRequest, String reason) {
		String scope = null;
		int code = 0;
		Map<String, Object> map = new HashMap<>();
		map.put("date",processRequestDate(approvalRequest.getEffectiveDate()));
		map.put("hour",approvalRequest.getHourLength());
		if (approvalRequest.getApprovalStatus().byteValue() == ApprovalStatus.AGREEMENT.getCode()) {
			scope = ApprovalNotificationTemplateCode.SCOPE;
			code = ApprovalNotificationTemplateCode.OVERTIME_APPROVED;
		}else {
			scope = ApprovalNotificationTemplateCode.SCOPE;
			code = ApprovalNotificationTemplateCode.OVERTIME_REJECTED;
			map.put("reason", StringUtils.isBlank(reason)?approvalRequest.getReason():reason);
			map.put("approver", approvalService.getUserName(approvalRequest.getOperatorUid(), approvalRequest.getOwnerId()));
		}
		return localeTemplateService.getLocaleTemplateString(scope, code, UserContext.current().getUser().getLocale(), map, "");
	}

	@Override
	public String processMessageToNextLevelBody(ApprovalRequest approvalRequest) {
		String scope = null;
		int code = 0;
		Map<String, Object> map = new HashMap<>();
		map.put("creatorName", approvalService.getUserName(approvalRequest.getCreatorUid(), approvalRequest.getOwnerId()));
		map.put("date",processRequestDate(approvalRequest.getEffectiveDate()));
		map.put("hour",approvalRequest.getHourLength());
		//当前级别为0表示用户刚提交
		if (approvalRequest.getCurrentLevel().byteValue() == (byte)0) {
			scope = ApprovalNotificationTemplateCode.SCOPE;
			code = ApprovalNotificationTemplateCode.OVERTIME_COMMIT_REQUEST;
		}else {
			scope = ApprovalNotificationTemplateCode.SCOPE;
			code = ApprovalNotificationTemplateCode.OVERTIME_TO_NEXT_LEVEL;
			map.put("approver", approvalService.getUserName(approvalRequest.getOperatorUid(), approvalRequest.getOwnerId()));
		}
		return localeTemplateService.getLocaleTemplateString(scope, code, UserContext.current().getUser().getLocale(), map, "");
	}
	

	@Override
	public String ApprovalLogAndFlowOfRequestResponseTitle(
			ApprovalRequest a) { 
		Map<String, Object> map = new HashMap<>();
		 
		 
		String scope = ApprovalLogTitleTemplateCode.SCOPE;
		int code = ApprovalLogTitleTemplateCode.OVERTIME_MAIN_TITLE ; 
		map.put("date",processRequestDate(a.getEffectiveDate()));
		map.put("hour",a.getHourLength());
		PunchDayLog pdl = this.punchProvider.getDayPunchLogByDate(a.getCreatorUid(), a.getOwnerId(), dateSF.format(a.getEffectiveDate()));
		map.put("punchLog",processPunchDetail(pdl)) ;
		String result = localeTemplateService.getLocaleTemplateString(scope, code, UserContext.current().getUser().getLocale(), map, "");
		
		return result;
	}

	@Override
	public BriefApprovalRequestDTO processApprovalRequestByScene(ApprovalRequest approvalRequest) {
		BriefApprovalRequestDTO briefApprovalRequestDTO = super.processBriefApprovalRequest(approvalRequest);
		  
		briefApprovalRequestDTO.setTitle(ApprovalRequestTitle(approvalRequest));
		return briefApprovalRequestDTO;
	}

	private String ApprovalRequestTitle(ApprovalRequest a) {
		Map<String, Object> map = new HashMap<>(); 
		 
		// 初次提交
		String scope = ApprovalLogTitleTemplateCode.SCOPE;
		int code = ApprovalLogTitleTemplateCode.OVERTIME_TITLE;
		map.put("nickName","");
		map.put("date",processRequestDate(a.getEffectiveDate()));
		map.put("hour",a.getHourLength());
		 
		String result = localeTemplateService.getLocaleTemplateString(scope, code, UserContext.current().getUser().getLocale(), map, "");
		
		return result;
	}
}
