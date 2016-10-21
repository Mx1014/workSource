package com.everhomes.approval;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.news.AttachmentProvider;
import com.everhomes.rest.approval.ApprovalBasicInfoOfRequestDTO;
import com.everhomes.rest.approval.ApprovalOwnerInfo;
import com.everhomes.rest.approval.ApprovalServiceErrorCode;
import com.everhomes.rest.approval.ApprovalTypeTemplateCode;
import com.everhomes.rest.approval.BasicDescriptionDTO;
import com.everhomes.rest.approval.CreateApprovalRequestBySceneCommand;
import com.everhomes.rest.approval.RequestDTO;
import com.everhomes.rest.techpark.punch.PunchTimesPerDay;
import com.everhomes.techpark.punch.PunchDayLog;
import com.everhomes.techpark.punch.PunchProvider;
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

	private static final SimpleDateFormat mmDDSF = new SimpleDateFormat("MM-dd");

	private static final SimpleDateFormat minSecSF = new SimpleDateFormat("mm:ss");
	
	private static final SimpleDateFormat weekdaySF =  new SimpleDateFormat("EEEE",Locale.CHINA);
	 
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
		if (approvalRequests != null && approvalRequests.size()>0) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE, ApprovalServiceErrorCode.EFFECTIVE_DATE_HAS_REQUEST,
					"invalid parameters, EffectiveDate already has a request !" );
		}
		//创建
		ApprovalRequest approvalRequest = super.preProcessCreateApprovalRequest(userId, ownerInfo, cmd);
		 	
		return approvalRequest;
	}
	 
	@Override
	public List<RequestDTO> processListApprovalRequest(List<ApprovalRequest> approvalRequestList) {
		List<RequestDTO> resultList = approvalRequestList.stream().map(a->{
			RequestDTO exceptionRequest = new RequestDTO();
			
			exceptionRequest.setRequestId(a.getId());  
			exceptionRequest.setNickName(approvalService.getUserName(a.getCreatorUid(), a.getOwnerId()));
			exceptionRequest.setReason(a.getReason()); 
			exceptionRequest.setRequestInfo(processRequestInfo(a.getEffectiveDate(),a.getHourLength()));
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
		approvalBasicInfo.setDescriptionJson(dto);
		return approvalBasicInfo;
	}
	private String processPunchDetail(PunchDayLog pdl) {
		String punchDetail = null;
		if(PunchTimesPerDay.TWICE.getCode().equals(pdl.getPunchTimesPerDay())){
			if(null != pdl.getArriveTime() ){
				punchDetail = minSecSF.format(pdl.getArriveTime());
				if(null != pdl.getLeaveTime() )
					punchDetail  = punchDetail +"/"+ minSecSF.format(pdl.getLeaveTime());
			}else{
				punchDetail = "无";
			}
			
		}
		else if(PunchTimesPerDay.FORTH.getCode().equals(pdl.getPunchTimesPerDay())){
			if(null != pdl.getArriveTime() ){
				punchDetail = minSecSF.format(pdl.getArriveTime());
				if(null != pdl.getNoonLeaveTime() )
					punchDetail  = punchDetail +"/"+ minSecSF.format(pdl.getNoonLeaveTime());
				}
			else
				punchDetail = "无";
			punchDetail += "|";
			if(null != pdl.getAfternoonArriveTime() ){
				punchDetail = minSecSF.format(pdl.getAfternoonArriveTime());
				if(null != pdl.getLeaveTime() )
					punchDetail  = punchDetail +"/"+ minSecSF.format(pdl.getLeaveTime());
				}
			else
				punchDetail = "无";
			
		}
		return punchDetail;
	}

	private String processRequestInfo(Date effectiveDate, Double hourLength) {
		return mmDDSF.format(effectiveDate)+"("+weekdaySF.format(effectiveDate)+") "+hourLength+"小时";
	}
 

	
	
}
