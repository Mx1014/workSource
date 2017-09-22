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
import com.everhomes.flow.FlowCase;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.news.AttachmentProvider;
import com.everhomes.rest.approval.ApprovalBasicInfoOfRequestDTO;
import com.everhomes.rest.approval.ApprovalExceptionContent;
import com.everhomes.rest.approval.ApprovalLogTitleTemplateCode;
import com.everhomes.rest.approval.ApprovalNotificationTemplateCode;
import com.everhomes.rest.approval.ApprovalOwnerInfo;
import com.everhomes.rest.approval.ApprovalServiceErrorCode;
import com.everhomes.rest.approval.ApprovalStatus;
import com.everhomes.rest.approval.ApprovalTypeTemplateCode;
import com.everhomes.rest.approval.BasicDescriptionDTO;
import com.everhomes.rest.approval.BriefApprovalRequestDTO;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.CreateApprovalRequestBySceneCommand;
import com.everhomes.rest.approval.ListApprovalLogAndFlowOfRequestBySceneResponse;
import com.everhomes.rest.approval.RequestDTO;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseEntityType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.techpark.punch.ExceptionStatus;
import com.everhomes.rest.techpark.punch.PunchRquestType;
import com.everhomes.rest.techpark.punch.PunchStatus;
import com.everhomes.rest.techpark.punch.PunchTimesPerDay;
import com.everhomes.rest.techpark.punch.ViewFlags;
import com.everhomes.server.schema.tables.pojos.EhApprovalAttachments;
import com.everhomes.techpark.punch.PunchConstants;
import com.everhomes.techpark.punch.PunchDayLog;
import com.everhomes.techpark.punch.PunchExceptionApproval;
import com.everhomes.techpark.punch.PunchExceptionRequest;
import com.everhomes.techpark.punch.PunchProvider;
import com.everhomes.techpark.punch.PunchService;
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
	private PunchService punchService;

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
		BasicDescriptionDTO description = new BasicDescriptionDTO();
		ApprovalExceptionContent content = JSONObject.parseObject(approvalRequest.getContentJson(), ApprovalExceptionContent.class);
		description.setPunchDate(new Timestamp(content.getPunchDate()));
		description.setPunchIntervalNo(content.getPunchIntervalNo());
		PunchDayLog pdl = this.punchProvider.getDayPunchLogByDate(approvalRequest.getCreatorUid(), 
				approvalRequest.getOwnerId(), dateSF.format(new Date(approvalRequest.getLongTag1())));
		description.setPunchDetail(processPunchDetail(pdl,content));
		description.setPunchStatusName(content.getPunchStatusName());
		
		approvalBasicInfo.setDescriptionJson(approvalRequest.getContentJson());
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
		if (approvalExceptionContent.getPunchDate() == null ) {
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
		PunchExceptionRequest punchExceptionRequest = getPunchExceptionRequest(userId, ownerInfo.getOwnerId(), approvalExceptionContent.getPunchDate(), approvalExceptionContent.getPunchIntervalNo()); 
		if (punchExceptionRequest != null) {
			approvalRequest.setId(punchExceptionRequest.getRequestId());
			//因为异常申请，再次申请时是同一张单据，所以需要删除之前申请的附件
			attachmentProvider.deleteAttachmentByOwnerId(EhApprovalAttachments.class, approvalRequest.getId());
		}
		
		return approvalRequest;
	}
	
	@Override
	public String postProcessCreateApprovalRequest(Long userId, ApprovalOwnerInfo ownerInfo, ApprovalRequest approvalRequest,
			CreateApprovalRequestBySceneCommand cmd) {
		ApprovalExceptionContent approvalExceptionContent = JSONObject.parseObject(cmd.getContentJson(), ApprovalExceptionContent.class);
		//处理考勤
		//插入一条记录.让用户查看考勤的时候 看到的是查看异常申请而不是添加异常申请
		PunchExceptionRequest punchExceptionRequest = getPunchExceptionRequest(userId, ownerInfo.getOwnerId(), approvalExceptionContent.getPunchDate(), approvalExceptionContent.getPunchIntervalNo()); 
		if (punchExceptionRequest != null) {
			punchExceptionRequest.setDescription(approvalRequest.getReason());
			punchProvider.updatePunchExceptionRequest(punchExceptionRequest);
		} else {
			createPunchExceptionRequest(userId, ownerInfo, approvalRequest, approvalExceptionContent);
		}
		//添加工作流
		//异常日期：${exceptionDate}\n打卡详情：${punchDetail}
		ApprovalExceptionContent content = JSONObject.parseObject(approvalRequest.getContentJson(), ApprovalExceptionContent.class);
		 
    	Map<String, String> map = new HashMap<String, String>();  
    	SimpleDateFormat dateSF = new SimpleDateFormat("MM-dd(E)");
        map.put("exceptionDate",dateSF.format(new Date(content.getPunchDate()))); 
        map.put("punchDetail", content.getPunchDetail() ); 
		String contentString = localeTemplateService.getLocaleTemplateString(PunchConstants.PUNCH_FLOW_CONTEXT_SCOPE ,
				 approvalRequest.getApprovalType().intValue() , "zh_CN", map, "");
		FlowCase flowCase = createflowCase(approvalRequest, contentString);
		String url = processFlowURL(flowCase.getId(), FlowUserType.APPLIER.getCode(), flowCase.getModuleId());
		return url;
	}

	@Override
	public List<FlowCaseEntity> getFlowCaseEntities(ApprovalRequest approvalRequest){
		List<FlowCaseEntity> entities = super.getFlowCaseEntities(approvalRequest); 
		FlowCaseEntity e = new FlowCaseEntity();  
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
		e.setKey(this.localeStringService.getLocalizedString(PunchConstants.PUNCH_FLOW_SCOPE,"requestDate", PunchConstants.locale, ""));
    	SimpleDateFormat dateSF = new SimpleDateFormat("MM-dd(E)");
		e.setValue(dateSF.format(approvalRequest.getCreateTime())); 
		entities.add(e); 
		
		ApprovalExceptionContent content = JSONObject.parseObject(approvalRequest.getContentJson(), ApprovalExceptionContent.class);
		e = new FlowCaseEntity();  
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
		e.setKey(this.localeStringService.getLocalizedString(PunchConstants.PUNCH_FLOW_SCOPE,"punchDetail", PunchConstants.locale, ""));
		e.setValue(content.getPunchDetail()); 
		entities.add(e); 
		entities.addAll(getPostFlowEntities(approvalRequest));
		return entities;
		
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
		punchExceptionRequest.setPunchIntervalNo(approvalExceptionContent.getPunchIntervalNo());
		punchProvider.createPunchExceptionRequest(punchExceptionRequest);
	}
	
	private PunchExceptionRequest getPunchExceptionRequest(Long userId, Long ownerId, Long punchDate, Integer PunchIntervalNo){
		//异常申请的重新申请是同一张单据，所以需要检查一下是否之前申请过，如果申请过则更新之前的
		return punchProvider.findPunchExceptionRequest(userId, ownerId, punchDate, PunchIntervalNo);
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
		PunchExceptionApproval punchExceptionApproval = punchProvider.findPunchExceptionApproval(approvalRequest.getCreatorUid(), approvalRequest.getOwnerId(),
				new Date(approvalExceptionContent.getPunchDate()) );
		if (punchExceptionApproval != null) {
			String statusList = punchExceptionApproval.getApprovalStatusList();
			if(null == statusList){
				statusList = createStatusList(punchExceptionApproval.getPunchTimesPerDay(), approvalExceptionContent.getPunchIntervalNo());
			}else{
				String[] statusArray = statusList.split(PunchConstants.STATUS_SEPARATOR);
				statusArray[approvalExceptionContent.getPunchIntervalNo()-1] = String.valueOf(ExceptionStatus.NORMAL.getCode());
				statusList =statusArray[0];
				for(int i =1;i<punchExceptionApproval.getPunchTimesPerDay()/2;i++) {
					statusList = statusList + PunchConstants.STATUS_SEPARATOR + statusArray[i];
				}
			}
			punchExceptionApproval.setApprovalStatusList(statusList);
			punchExceptionApproval.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			punchExceptionApproval.setOperatorUid(UserContext.current().getUser().getId());
			punchProvider.updatePunchExceptionApproval(punchExceptionApproval);
		}else {
			punchExceptionApproval = new PunchExceptionApproval();
			String statusList = createStatusList(punchExceptionApproval.getPunchTimesPerDay(), approvalExceptionContent.getPunchIntervalNo());
			punchExceptionApproval.setApprovalStatusList(statusList);
			punchExceptionApproval.setUserId(approvalRequest.getCreatorUid());
			punchExceptionApproval.setEnterpriseId(approvalRequest.getOwnerId());
			punchExceptionApproval.setPunchDate(new Date(approvalExceptionContent.getPunchDate()));
			punchExceptionApproval.setCreatorUid(UserContext.current().getUser().getId());
			punchExceptionApproval.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			punchExceptionApproval.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			punchExceptionApproval.setOperatorUid(UserContext.current().getUser().getId());
			punchExceptionApproval.setViewFlag(ViewFlags.NOTVIEW.getCode());
			punchProvider.createPunchExceptionApproval(punchExceptionApproval);
		}
		
		//更新eh_punch_day_logs中的exception_status字段
		PunchDayLog punchDayLog = punchProvider.findPunchDayLog(approvalRequest.getCreatorUid(), approvalRequest.getOwnerId(), new Date(approvalExceptionContent.getPunchDate()));
		if (punchDayLog != null) {
//			if (punchDayLog.getPunchTimesPerDay().byteValue() == PunchTimesPerDay.TWICE.getCode().byteValue() && punchExceptionApproval.getApprovalStatus() != null && punchExceptionApproval.getApprovalStatus().byteValue() == PunchStatus.NORMAL.getCode()) {
//				punchDayLog.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
//				punchProvider.updatePunchDayLog(punchDayLog);
//			}else if (punchDayLog.getPunchTimesPerDay().byteValue() == PunchTimesPerDay.FORTH.getCode().byteValue() &&
//					punchExceptionApproval.getMorningApprovalStatus() != null && (punchExceptionApproval.getMorningApprovalStatus().byteValue() == PunchStatus.NORMAL.getCode()
//					||punchDayLog.getMorningStatus().byteValue() == PunchStatus.NORMAL.getCode())&&
//					punchExceptionApproval.getAfternoonApprovalStatus() != null && punchExceptionApproval.getAfternoonApprovalStatus().byteValue() == PunchStatus.NORMAL.getCode()
//					||punchDayLog.getAfternoonStatus().byteValue() == PunchStatus.NORMAL.getCode()) {
//				punchDayLog.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
//				punchProvider.updatePunchDayLog(punchDayLog);
//			}
			punchDayLog.setApprovalStatusList(punchExceptionApproval.getApprovalStatusList());
			String[] statusArray = punchDayLog.getStatusList().split(PunchConstants.STATUS_SEPARATOR);
			punchDayLog.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
			for(int i =0;i<punchExceptionApproval.getPunchTimesPerDay()/2;i++) {
				if (statusArray[i].equals(String.valueOf(ExceptionStatus.EXCEPTION.getCode()))) {
					punchDayLog.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
					break;
				}
			}
			punchProvider.updatePunchDayLog(punchDayLog);
		}
	}

	private String createStatusList(Byte punchTimesPerDay, Integer PunchIntervalNo) {
		String statusList = "";
		for (int i = 0; i < PunchIntervalNo / 2; i++) {
			String status = String.valueOf(ExceptionStatus.EXCEPTION.getCode());
			if (i == PunchIntervalNo - 1)
				status = String.valueOf(ExceptionStatus.NORMAL.getCode());

			if (i == 0)
				statusList = status;
			else
				statusList = statusList + PunchConstants.STATUS_SEPARATOR + status;
		}
		return statusList;
	}

	@Override
	public List<RequestDTO> processListApprovalRequest(List<ApprovalRequest> approvalRequestList) {
		List<RequestDTO> resultList = approvalRequestList.stream().map(a->{
			RequestDTO exceptionRequest = new RequestDTO();
			ApprovalExceptionContent approvalExceptionContent = JSONObject.parseObject(a.getContentJson(), ApprovalExceptionContent.class);
			exceptionRequest.setRequestId(a.getId());
			exceptionRequest.setPunchDate(new Timestamp(approvalExceptionContent.getPunchDate()));
			exceptionRequest.setPunchIntervalNo(approvalExceptionContent.getPunchIntervalNo());
			exceptionRequest.setNickName(approvalService.getUserName(a.getCreatorUid(), a.getOwnerId()));
			exceptionRequest.setReason(a.getReason());
			exceptionRequest.setPunchStatusName(approvalExceptionContent.getPunchStatusName());
			return exceptionRequest;
		}).collect(Collectors.toList());
		
		
		return resultList;
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

	@Override
	public BriefApprovalRequestDTO processBriefApprovalRequest(ApprovalRequest approvalRequest) {
		BriefApprovalRequestDTO briefApprovalRequestDTO = super.processBriefApprovalRequest(approvalRequest);
		  
		briefApprovalRequestDTO.setTitle(processBriefRequestTitle(approvalRequest  ));
		return briefApprovalRequestDTO;
	}
	private String processBriefRequestTitle(ApprovalRequest a  ) { 
		Map<String, Object> map = new HashMap<>();
		 
		 
		String scope = ApprovalLogTitleTemplateCode.SCOPE;
		int code = ApprovalLogTitleTemplateCode.EXCEPTION_TITLE;
		map.put("nickName",approvalService.getUserName(a.getCreatorUid(), a.getOwnerId()) );
		ApprovalExceptionContent content = JSONObject.parseObject(a.getContentJson(), ApprovalExceptionContent.class);
		map.put("date",processRequestDate(new Date(a.getLongTag1()) ,content));
		 
		String result = localeTemplateService.getLocaleTemplateString(scope, code, UserContext.current().getUser().getLocale(), map, "");
		
		return result;
	}
	@Override
	public String ApprovalLogAndFlowOfRequestResponseTitle(
			ApprovalRequest a) { 
		Map<String, Object> map = new HashMap<>();
		 
		String scope = ApprovalLogTitleTemplateCode.SCOPE;
		int code = ApprovalLogTitleTemplateCode.EXCEPTION_MAIN_TITLE; 
		ApprovalExceptionContent content = JSONObject.parseObject(a.getContentJson(), ApprovalExceptionContent.class);
		map.put("date",processRequestDate(new Date(a.getLongTag1()) ,content));
		PunchDayLog pdl = this.punchProvider.getDayPunchLogByDate(a.getCreatorUid(), a.getOwnerId(), dateSF.format(new Date(a.getLongTag1())));
		map.put("punchLog",processPunchDetail(pdl,content)) ;
//		if(null == content || content.getPunchIntervalNo().equals(PunchIntervalNo.ALL_DAY.getCode()))
			map.put("punchStatus", punchService.statusToString(pdl.getStatus()));
//		else if(content.getPunchIntervalNo().equals(PunchIntervalNo.MORNING.getCode()))
//			map.put("punchStatus", punchService.statusToString(pdl.getMorningStatus()) );
//		else if(content.getPunchIntervalNo().equals(PunchIntervalNo.AFTERNOON.getCode()))
//			map.put("punchStatus", punchService.statusToString(pdl.getAfternoonStatus()));
		String result = localeTemplateService.getLocaleTemplateString(scope, code, UserContext.current().getUser().getLocale(), map, "");
		
		return result;
	}
	 
	public String processRequestDate(Date effectiveDate, ApprovalExceptionContent content ) {
		return mmDDSF.format(effectiveDate)+"("+weekdaySF.format(effectiveDate)+") " ;
	}
	private static final SimpleDateFormat minSecSF = new SimpleDateFormat("HH:mm");
	  
	protected String processPunchDetail(PunchDayLog pdl, ApprovalExceptionContent content) {
		String punchDetail = null;
		if(null == pdl )
			return "无";
//		if(PunchTimesPerDay.TWICE.getCode().equals(pdl.getPunchTimesPerDay())){
			if(null != pdl.getArriveTime() ){
				punchDetail = minSecSF.format(pdl.getArriveTime());
				if(null != pdl.getLeaveTime() )
					punchDetail  = punchDetail +"/"+ minSecSF.format(pdl.getLeaveTime());
			}else{
				punchDetail = "无";
			}
			
//		}
//		else if(PunchTimesPerDay.FORTH.getCode().equals(pdl.getPunchTimesPerDay())){
//
//			if(null != content){
//				if(content.getPunchIntervalNo().equals(PunchIntervalNo.MORNING.getCode()) && null != pdl.getArriveTime() ){
//					punchDetail = minSecSF.format(pdl.getArriveTime());
//					if(null != pdl.getNoonLeaveTime() )
//						punchDetail  = punchDetail +"/"+ minSecSF.format(pdl.getNoonLeaveTime());
//					}
//				else if(content.getPunchIntervalNo().equals(PunchIntervalNo.AFTERNOON.getCode()) &&null != pdl.getAfternoonArriveTime() ){
//					punchDetail = minSecSF.format(pdl.getAfternoonArriveTime());
//					if(null != pdl.getLeaveTime() )
//						punchDetail  = punchDetail +"/"+ minSecSF.format(pdl.getLeaveTime());
//					}
//				else
//					punchDetail = "无";
//				}
//
//		}
		return punchDetail;
	}
	@Override
	public ListApprovalLogAndFlowOfRequestBySceneResponse processListApprovalLogAndFlowOfRequestBySceneResponse(
			ListApprovalLogAndFlowOfRequestBySceneResponse result,
			ApprovalRequest approvalRequest) { 
		ApprovalExceptionContent content = JSONObject.parseObject(approvalRequest.getContentJson(), ApprovalExceptionContent.class);
		result.setPunchDate( content.getPunchDate() );
		result.setPunchIntervalNo(content.getPunchIntervalNo()); 
		PunchDayLog pdl = this.punchProvider.getDayPunchLogByDate(approvalRequest.getCreatorUid(), 
				approvalRequest.getOwnerId(), dateSF.format(new Date(approvalRequest.getLongTag1())));
		result.setPunchDetail(processPunchDetail(pdl,content));
		result.setPunchStatusName(content.getPunchStatusName());
		return null;
	}

	@Override
	public BriefApprovalRequestDTO processApprovalRequestByScene(ApprovalRequest approvalRequest) {
		BriefApprovalRequestDTO briefApprovalRequestDTO = super.processBriefApprovalRequest(approvalRequest);
		  
		briefApprovalRequestDTO.setTitle(ApprovalLogAndFlowOfRequestResponseTitle(approvalRequest  ));
		return briefApprovalRequestDTO;
	}
}
