package com.everhomes.approval;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.rest.approval.ApprovalBasicInfoOfRequestDTO;
import com.everhomes.rest.approval.ApprovalOwnerInfo;
import com.everhomes.rest.approval.ApprovalStatus;
import com.everhomes.rest.approval.BriefApprovalRequestDTO;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.CreateApprovalRequestBySceneCommand;
import com.everhomes.rest.approval.ListApprovalLogAndFlowOfRequestBySceneResponse;
import com.everhomes.rest.approval.RequestDTO;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.techpark.punch.PunchTimesPerDay;
import com.everhomes.techpark.punch.PunchDayLog;
import com.everhomes.util.DateHelper;
import com.everhomes.util.ListUtils;
import com.everhomes.util.WebTokenGenerator;

/**
 * 
 * <ul>
 * 用于审批的默认handler
 * </ul>
 */
@Component(ApprovalRequestDefaultHandler.APPROVAL_REQUEST_DEFAULT_HANDLER_NAME)
public class ApprovalRequestDefaultHandler implements ApprovalRequestHandler {
	
	static final String APPROVAL_REQUEST_DEFAULT_HANDLER_NAME = ApprovalRequestHandler.APPROVAL_REQUEST_OBJECT_PREFIX + "Default";
	
	@Autowired
	private ApprovalCategoryProvider approvalCategoryProvider;
	
	@Autowired
	private ApprovalService approvalService;
	

	@Autowired
	LocaleTemplateService localeTemplateService;

	@Autowired
	LocaleStringProvider localeStringProvider;
	@Override
	public ApprovalBasicInfoOfRequestDTO processApprovalBasicInfoOfRequest(ApprovalRequest approvalRequest) {
		return new ApprovalBasicInfoOfRequestDTO(approvalRequest.getApprovalType(), approvalRequest.getApprovalStatus());
	}

	@Override
	public BriefApprovalRequestDTO processBriefApprovalRequest(ApprovalRequest approvalRequest) {
		BriefApprovalRequestDTO briefApprovalRequestDTO = new BriefApprovalRequestDTO();
		briefApprovalRequestDTO.setRequestToken(WebTokenGenerator.getInstance().toWebToken(approvalRequest.getId()));
		briefApprovalRequestDTO.setApprovalType(approvalRequest.getApprovalType());
		if (approvalRequest.getCategoryId() != null) {
			ApprovalCategory approvalCategory = approvalCategoryProvider.findApprovalCategoryById(approvalRequest.getCategoryId());
			if (approvalCategory != null) {
				briefApprovalRequestDTO.setCategoryName(approvalCategory.getCategoryName());
			}
		}
		briefApprovalRequestDTO.setApprovalStatus(approvalRequest.getApprovalStatus());
		briefApprovalRequestDTO.setCreateTime(approvalRequest.getCreateTime());
		briefApprovalRequestDTO.setReason(approvalRequest.getReason());
		if (approvalRequest.getTimeFlag().byteValue() == TrueOrFalseFlag.TRUE.getCode()) {
			briefApprovalRequestDTO.setTimeRangeList(approvalService.listTimeRangeByRequestId(approvalRequest.getId()));
		}
		if (approvalRequest.getAttachmentFlag().byteValue() == TrueOrFalseFlag.TRUE.getCode()) {
			briefApprovalRequestDTO.setAttachmentList(approvalService.listAttachmentByRequestId(approvalRequest.getId()));
		}
		
		return briefApprovalRequestDTO;
	}

	@Override
	public ApprovalRequest preProcessCreateApprovalRequest(Long userId, ApprovalOwnerInfo ownerInfo,
			CreateApprovalRequestBySceneCommand cmd) {
		return generateApprovalRequest(userId, ownerInfo, cmd);
	}

	@Override
	public void postProcessCreateApprovalRequest(Long userId, ApprovalOwnerInfo ownerInfo, ApprovalRequest approvalRequest,
			CreateApprovalRequestBySceneCommand cmd) {
	}
	

	private ApprovalRequest generateApprovalRequest(Long userId, ApprovalOwnerInfo ownerInfo, CreateApprovalRequestBySceneCommand cmd) {
		ApprovalRequest approvalRequest = new ApprovalRequest();
		if(null != cmd.getEffectiveDate())
			approvalRequest.setEffectiveDate(new Date(cmd.getEffectiveDate()));
		approvalRequest.setHourLength(cmd.getHourLength());
		approvalRequest.setNamespaceId(ownerInfo.getNamespaceId());
		approvalRequest.setOwnerType(ownerInfo.getOwnerType());
		approvalRequest.setOwnerId(ownerInfo.getOwnerId());
		approvalRequest.setApprovalType(cmd.getApprovalType());
		approvalRequest.setCategoryId(cmd.getCategoryId());
		approvalRequest.setReason(cmd.getReason());
		approvalRequest.setAttachmentFlag(ListUtils.isEmpty(cmd.getAttachmentList())?TrueOrFalseFlag.FALSE.getCode():TrueOrFalseFlag.TRUE.getCode());
		approvalRequest.setTimeFlag(ListUtils.isEmpty(cmd.getTimeRangeList())?TrueOrFalseFlag.FALSE.getCode():TrueOrFalseFlag.TRUE.getCode());
		approvalRequest.setApprovalStatus(ApprovalStatus.WAITING_FOR_APPROVING.getCode());
		approvalRequest.setStatus(CommonStatus.ACTIVE.getCode());
		approvalRequest.setCreatorUid(userId);
		approvalRequest.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		approvalRequest.setUpdateTime(approvalRequest.getCreateTime());
		approvalRequest.setOperatorUid(userId);
		approvalRequest.setFlowId(approvalService.getApprovalFlowByUser(ownerInfo.getOwnerType(), ownerInfo.getOwnerId(), userId, cmd.getApprovalType()).getId());
		approvalRequest.setCurrentLevel((byte) 0);
		approvalRequest.setNextLevel((byte) 1);
		return approvalRequest;
	}

	public static final SimpleDateFormat mmDDSF = new SimpleDateFormat("MM-dd");
	
	public static final SimpleDateFormat weekdaySF =  new SimpleDateFormat("EEEE",Locale.CHINA);
	 
	public String processRequestDate(Date effectiveDate ) {
		return mmDDSF.format(effectiveDate)+"("+weekdaySF.format(effectiveDate)+") ";
	}
 

	@Override
	public void processCancelApprovalRequest(ApprovalRequest approvalRequest) {
		
	}

	@Override
	public void processFinalApprove(ApprovalRequest approvalRequest) {
	}

	@Override
	public List<RequestDTO> processListApprovalRequest(List<ApprovalRequest> approvalRequestList) {
		return null;
	}

	@Override
	public String processMessageToCreatorBody(ApprovalRequest approvalRequest, String reason) {
		return null;
	}

	@Override
	public String processMessageToNextLevelBody(ApprovalRequest approvalRequest) {
		return null;
	}

	@Override
	public String ApprovalLogAndFlowOfRequestResponseTitle(
			ApprovalRequest approvalRequest) { 
		return null;
	} 

	private static final SimpleDateFormat minSecSF = new SimpleDateFormat("HH:mm");
	 

	protected String processPunchDetail(PunchDayLog pdl) {
		String punchDetail = null;
		if(null == pdl )
			return "无";
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

	@Override
	public ListApprovalLogAndFlowOfRequestBySceneResponse processListApprovalLogAndFlowOfRequestBySceneResponse(
			ListApprovalLogAndFlowOfRequestBySceneResponse result,
			ApprovalRequest approvalRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	public BriefApprovalRequestDTO processApprovalRequestByScene(
			ApprovalRequest approvalRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void calculateRangeStat(ApprovalRequest approvalRequest) {
		// TODO Auto-generated method stub
		// 取到所有的ranges-目前一个request对应一个
		// 找到时间
	}
	
}
