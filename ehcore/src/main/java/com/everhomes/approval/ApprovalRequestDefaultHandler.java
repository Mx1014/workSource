package com.everhomes.approval;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.news.Attachment;
import com.everhomes.news.AttachmentProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rentalv2.RentalNotificationTemplateCode;
import com.everhomes.rentalv2.RentalResourceType;
import com.everhomes.rentalv2.Rentalv2Controller;
import com.everhomes.rentalv2.Rentalv2ServiceImpl;
import com.everhomes.rest.approval.ApprovalBasicInfoOfRequestDTO;
import com.everhomes.rest.approval.ApprovalOwnerInfo;
import com.everhomes.rest.approval.ApprovalStatus;
import com.everhomes.rest.approval.BriefApprovalRequestDTO;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.CreateApprovalRequestBySceneCommand;
import com.everhomes.rest.approval.ListApprovalLogAndFlowOfRequestBySceneResponse;
import com.everhomes.rest.approval.RequestDTO;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseEntityType;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.FlowReferType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.techpark.punch.PunchTimesPerDay;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.server.schema.tables.pojos.EhApprovalAttachments;
import com.everhomes.techpark.punch.PunchConstants;
import com.everhomes.techpark.punch.PunchController;
import com.everhomes.techpark.punch.PunchDayLog;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
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

	
	protected static final Logger LOGGER = LoggerFactory.getLogger(ApprovalRequestDefaultHandler.class);
	
	@Autowired
	private ApprovalCategoryProvider approvalCategoryProvider;

	@Autowired
    private FlowService flowService;
    @Autowired
    private FlowCaseProvider flowCaseProvider;
    @Autowired
    private UserProvider userProvider;
	@Autowired
	private ApprovalService approvalService;

	@Autowired
	private ContentServerService contentServerService;
	
	@Autowired
	private AttachmentProvider attachmentProvider;

	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	LocaleTemplateService localeTemplateService;

	@Autowired
	LocaleStringService localeStringService;
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

	public static final String REFER_TYPE= FlowReferType.PUNCH_APPROVAL.getCode();
	@Override
	public String postProcessCreateApprovalRequest(Long userId, ApprovalOwnerInfo ownerInfo, ApprovalRequest approvalRequest,
			CreateApprovalRequestBySceneCommand cmd) {
		return null;
	}
	
	
    public static String processFlowURL(Long flowCaseId, String flowUserType, Long moduleId) { 
		return "zl://workflow/detail?flowCaseId="+flowCaseId+"&flowUserType="+flowUserType+"&moduleId="+moduleId  ;
	}

	public FlowCase createflowCase(ApprovalRequest approvalRequest,String contentString ){
		String moduleType = FlowModuleType.NO_MODULE.getCode();
		Long ownerId = approvalRequest.getOwnerId();
		
		String ownerType = FlowOwnerType.EhOrganizations.getCode();
    	Flow flow = flowService.getEnabledFlow(approvalRequest.getNamespaceId(),PunchConstants.PUNCH_MODULE_ID, moduleType, ownerId, ownerType);
    	LOGGER.debug("parames : " +approvalRequest.getNamespaceId()+"*"+ PunchConstants.PUNCH_MODULE_ID+"*"+ moduleType+"*"+ ownerId+"*"+ ownerType );
    	LOGGER.debug("\n flow is "+flow);
    	if(null!=flow){
	    	CreateFlowCaseCommand cmd = new CreateFlowCaseCommand();
	    	cmd.setApplyUserId(approvalRequest.getCreatorUid());
	    	cmd.setFlowMainId(flow.getFlowMainId());
	    	cmd.setFlowVersion(flow.getFlowVersion());
	    	cmd.setReferId(approvalRequest.getId());
	    	cmd.setReferType(REFER_TYPE);
	    	OrganizationDTO organization = organizationService.getOrganizationById(approvalRequest.getOwnerId());
	    	cmd.setProjectId(organization.getCommunityId());
	    	cmd.setProjectType(EntityType.COMMUNITY.getCode());
	    	cmd.setCurrentOrganizationId(organization.getId());
			String title = localeStringService.getLocalizedString(PunchConstants.PUNCH_FLOW_TITLE_SCOPE, approvalRequest.getApprovalType().toString(), "zh_CN", "");
			cmd.setTitle(title);
	    	cmd.setContent(contentString); 
	    	FlowCase flowCase = flowService.createFlowCase(cmd);
	    	return flowCase;
    	}
    	return null;
	}
	@Override
	/**前置的详情entity -(发起人和联系电话)*/
	public List<FlowCaseEntity> getFlowCaseEntities(ApprovalRequest approvalRequest){
		List<FlowCaseEntity> entities = new ArrayList<>();
		FlowCaseEntity e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
		e.setKey(this.localeStringService.getLocalizedString(PunchConstants.PUNCH_FLOW_SCOPE,
				"creator", PunchConstants.locale, ""));
		User user =  userProvider.findUserById(approvalRequest.getCreatorUid());
		if (null != user)
			e.setValue(user.getNickName());
		else {
			LOGGER.debug("user is null...userId = " + approvalRequest.getCreatorUid());
			e.setValue("用户不在系统中");
		}
		entities.add(e);

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
		e.setKey(this.localeStringService.getLocalizedString(PunchConstants.PUNCH_FLOW_SCOPE,
				"contact", PunchConstants.locale, ""));
		UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(
				approvalRequest.getCreatorUid(), IdentifierType.MOBILE.getCode());
		if (null == userIdentifier) {
			LOGGER.debug("userIdentifier is null...userId = " + approvalRequest.getCreatorUid());
		} else {
			e.setValue(userIdentifier.getIdentifierToken());
		}
		entities.add(e);
		
		return entities;
	}
	/**后置的详情entity -(申请理由和图片)*/
	protected Collection<? extends FlowCaseEntity> getPostFlowEntities(ApprovalRequest approvalRequest) { 
		List<FlowCaseEntity> entities = new ArrayList<>();
		FlowCaseEntity e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
		e.setKey(this.localeStringService.getLocalizedString(PunchConstants.PUNCH_FLOW_SCOPE,
				"requestReson", PunchConstants.locale, ""));
		 
		e.setValue(approvalRequest.getReason()); 
		entities.add(e);

		List<Attachment> attachmentList = attachmentProvider.listAttachmentByOwnerId(EhApprovalAttachments.class, approvalRequest.getId());
		for(Attachment attachment : attachmentList){
			e = new FlowCaseEntity();
			e.setEntityType(FlowCaseEntityType.IMAGE.getCode());
			e.setKey(this.localeStringService.getLocalizedString(PunchConstants.PUNCH_FLOW_SCOPE,
					"attachment", PunchConstants.locale, ""));
			e.setValue( contentServerService.parserUri(attachment.getContentUri(),
					EntityType.USER.getCode(), UserContext.current().getUser().getId()));
			entities.add(e);
		}
		return entities;
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
