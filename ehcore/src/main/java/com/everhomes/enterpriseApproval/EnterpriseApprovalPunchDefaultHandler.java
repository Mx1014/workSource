package com.everhomes.enterpriseApproval;

import com.alibaba.fastjson.JSON;
import com.everhomes.approval.ApprovalCategory;
import com.everhomes.approval.ApprovalCategoryProvider;
import com.everhomes.approval.ApprovalServiceConstants;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalProvider;
import com.everhomes.general_approval.GeneralApprovalVal;
import com.everhomes.general_approval.GeneralApprovalValProvider;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.approval.ApprovalCategoryTimeSelectType;
import com.everhomes.rest.approval.ApprovalStatus;
import com.everhomes.rest.enterpriseApproval.ComponentAbnormalPunchValue;
import com.everhomes.rest.enterpriseApproval.ComponentAskForLeaveValue;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.general_approval.GeneralApprovalAttribute;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.techpark.punch.PunchOwnerType;
import com.everhomes.rest.techpark.punch.admin.UpdateVacationBalancesCommand;
import com.everhomes.techpark.punch.PunchExceptionRequest;
import com.everhomes.techpark.punch.PunchNotificationService;
import com.everhomes.techpark.punch.PunchProvider;
import com.everhomes.techpark.punch.PunchRule;
import com.everhomes.techpark.punch.PunchService;
import com.everhomes.techpark.punch.PunchVacationBalance;
import com.everhomes.techpark.punch.PunchVacationBalanceService;
import com.everhomes.techpark.punch.utils.PunchDateUtils;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * <ul>
 * 用于和考勤相关的审批的默认handler
 * </ul>
 */
@Component(EnterpriseApprovalPunchDefaultHandler.ENTERPRISE_APPROVAL_DEFAULT_HANDLER_NAME)
public class EnterpriseApprovalPunchDefaultHandler extends EnterpriseApprovalDefaultHandler {
	
	static final String ENTERPRISE_APPROVAL_DEFAULT_HANDLER_NAME = EnterpriseApprovalHandler.ENTERPRISE_APPROVAL_PREFIX + "PunchDefault";
	@Autowired
	protected PunchProvider punchProvider;
	@Autowired
	protected PunchService punchService;
	@Autowired
	protected GeneralApprovalProvider generalApprovalProvider;
	@Autowired
	protected GeneralApprovalValProvider generalApprovalValProvider;
	@Autowired
	protected GeneralFormProvider generalFormProvider;
	@Autowired
	private ApprovalCategoryProvider approvalCategoryProvider;
	@Autowired
	protected OrganizationProvider organizationProvider;
	@Autowired
	private LocaleStringService localeStringService;
	@Autowired
	private PunchVacationBalanceService punchVacationBalanceService;
	@Autowired
	private PunchNotificationService punchNotificationService;


	protected static ThreadLocal<SimpleDateFormat> datetimeSF = new ThreadLocal<SimpleDateFormat>(){
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	protected static ThreadLocal<SimpleDateFormat> dateSF = new ThreadLocal<SimpleDateFormat>(){
    	protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
	protected static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseApprovalPunchDefaultHandler.class);

	@Override
	public void onApprovalCreated(FlowCase flowCase) {
		GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(flowCase.getReferId());

		//建立一个request
		PunchExceptionRequest request = new PunchExceptionRequest();
		request.setEnterpriseId(ga.getOrganizationId());
		//初始状态是等待审批
		request.setStatus(ApprovalStatus.WAITING_FOR_APPROVING.getCode());
		request.setUserId(flowCase.getApplyUserId());
		request.setApprovalAttribute(ga.getApprovalAttribute());
		request.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		request.setCreatorUid(UserContext.currentUserId());
		//用工作流的id 作為表示是哪個審批
		request.setRequestId(flowCase.getId());
		//分别处理
		if (punchService.getTimeIntervalApprovalAttribute().contains(ga.getApprovalAttribute())) {

			GeneralApprovalVal val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndFeildType(flowCase.getId(),
					ga.getApprovalAttribute());
			ComponentAskForLeaveValue valDTO = JSON.parseObject(val.getFieldStr3(), ComponentAskForLeaveValue.class);
			request.setBeginTime(parseStartTime(ga.getOrganizationId(), flowCase.getApplyUserId(), valDTO.getStartTime()));
			request.setEndTime(parseEndTime(ga.getOrganizationId(), flowCase.getApplyUserId(), valDTO.getEndTime()));
			request.setDurationDay(new BigDecimal(String.valueOf(valDTO.getDuration() != null ? valDTO.getDuration() : 0)));
			request.setDurationMinute(valDTO.getDurationInMinute());
			request.setCategoryId(valDTO.getRestId());
		} else if (GeneralApprovalAttribute.fromCode(ga.getApprovalAttribute()) == GeneralApprovalAttribute.ABNORMAL_PUNCH) {
			GeneralApprovalVal val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndFeildType(flowCase.getId(),
					GeneralFormFieldType.ABNORMAL_PUNCH.getCode());
			ComponentAbnormalPunchValue valDTO = JSON.parseObject(val.getFieldStr3(), ComponentAbnormalPunchValue.class);
			request.setPunchDate(java.sql.Date.valueOf(valDTO.getAbnormalDate()));
			request.setPunchType(valDTO.getPunchType());
			request.setPunchIntervalNo(valDTO.getPunchIntervalNo());
		}

		punchProvider.createPunchExceptionRequest(request);
		refreshPunchDayLog(flowCase, ga, request);
		String description = localeStringService.getLocalizedString(ApprovalServiceConstants.SCOPE, String.valueOf(ApprovalServiceConstants.VACATION_BALANCE_DEC_FOR_ASK_FOR_LEAVE), UserContext.current().getUser().getLocale(), "");
		updateVacationBalance(flowCase.getApplyUserId(), ga, request, (byte) -1, description);
	}

	@Override
	public void onFlowCaseAbsorted(FlowCaseState ctx) {
		FlowCase flowCase = ctx.getRootState().getFlowCase();
		GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(flowCase.getReferId());
		PunchExceptionRequest request = punchProvider.findPunchExceptionRequestByRequestId(ga.getOrganizationId(), flowCase.getApplyUserId(), flowCase.getId());
		if (null == request) {
			return;
		}
		int code = 0;
		if (UserContext.current().getUser().getId().equals(request.getUserId()) && ctx.getCurrentEvent().getUserType() == FlowUserType.APPLIER) {
			//如果是自己取消的,删除request
			code = ApprovalServiceConstants.VACATION_BALANCE_INCR_FOR_CANCEL_ASK_FOR_LEAVE;
			punchProvider.deletePunchExceptionRequest(request);
		} else {
			//否则request变成reject
			code = ApprovalServiceConstants.VACATION_BALANCE_INCR_FOR_REJECT_ASK_FOR_LEAVE;
			request.setStatus(ApprovalStatus.REJECTION.getCode());
			punchProvider.updatePunchExceptionRequest(request);
		}

		String description = localeStringService.getLocalizedString(ApprovalServiceConstants.SCOPE, String.valueOf(code), UserContext.current().getUser().getLocale(), "");
		// 返还假期余额
		updateVacationBalance(flowCase.getApplyUserId(), ga, request, (byte) 1, description);
	}

	@Override
	public void onFlowCaseDeleted(FlowCase flowCase) {
		GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(flowCase.getReferId());
		PunchExceptionRequest request = punchProvider.findPunchExceptionRequestByRequestId(ga.getOrganizationId(), flowCase.getApplyUserId(), flowCase.getId());
		if (request == null) {
			return;
		}
		// 如果流程删除之前是审批通过状态，则删除以后，需要重新校准考勤状态，否则不需要
		boolean showRefreshPunchDayLog = ApprovalStatus.AGREEMENT == ApprovalStatus.fromCode(request.getStatus());
		boolean showUpdateVacationBalance = ApprovalStatus.REJECTION != ApprovalStatus.fromCode(request.getStatus());
		punchProvider.deletePunchExceptionRequest(request);
		// 审批单被驳回后已经退回余额，所以删除以后不需要再退
		if (showUpdateVacationBalance) {
			String description = localeStringService.getLocalizedString(ApprovalServiceConstants.SCOPE, String.valueOf(ApprovalServiceConstants.VACATION_BALANCE_INCR_FOR_REJECT_ASK_FOR_LEAVE), UserContext.current().getUser().getLocale(), "");
			// 返还假期余额
			updateVacationBalance(flowCase.getApplyUserId(), ga, request, (byte) 1, description);
		}
		if (!showRefreshPunchDayLog) {
			return;
		}
		try {
			// 统计年假使用合计和调休使用合计
			addVacationBalanceHistoryCount(flowCase.getApplyUserId(), ga, request, (byte) -1);

			Date startDay = PunchDateUtils.getDateBeginDate(new Date(request.getBeginTime().getTime()));
			Date endDay = PunchDateUtils.getDateBeginDate(new Date(request.getEndTime().getTime()));
			punchService.refreshPunchDayLog(request.getUserId(), request.getEnterpriseId(), startDay, endDay);
		} catch (Exception e) {
			LOGGER.error("refreshPunchDayLog error user_id = {}", flowCase.getApplyUserId());
		}
	}

	@Override
	public PunchExceptionRequest onFlowCaseEnd(FlowCase flowCase) {
		//通过就把状态置为已审批
		GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(flowCase.getReferId());
		PunchExceptionRequest request = punchProvider.findPunchExceptionRequestByRequestId(ga.getOrganizationId(), flowCase.getApplyUserId(), flowCase.getId());
		if (null == request) {
			return null;
		}
		request.setStatus(ApprovalStatus.AGREEMENT.getCode());
		punchProvider.updatePunchExceptionRequest(request);

		try {
			// 如果审批类型是-加班请假等,重刷影响日期的pdl
			if (punchService.getTimeIntervalApprovalAttribute().contains(ga.getApprovalAttribute())) {
				refreshPunchDayLog(flowCase, ga, request);
			}
			punchNotificationService.setPunchNotificationInvalidBackground(request, ga.getNamespaceId(), new Date());
		} catch (Exception e) {
			LOGGER.error("refreshPunchDayLog error user_id = {}", flowCase.getApplyUserId());
		}

		// 统计年假使用合计和调休使用合计
		addVacationBalanceHistoryCount(flowCase.getApplyUserId(), ga, request, (byte) 1);

		return request;
	}

	private void refreshPunchDayLog(FlowCase flowCase, GeneralApproval ga,
			PunchExceptionRequest request) {
		try{
			PunchRule pr = punchService.getPunchRule(PunchOwnerType.ORGANIZATION.getCode(), ga.getOrganizationId(), flowCase.getApplyUserId());
			if (pr != null) {
				Calendar punCalendar = Calendar.getInstance();
				punCalendar.setTime(request.getBeginTime());
				Date startDay = punchService.calculatePunchDate(punCalendar, request.getEnterpriseId(), request.getUserId());
				punCalendar.setTime(request.getEndTime());
				Date endDay = punchService.calculatePunchDate(punCalendar, request.getEnterpriseId(), request.getUserId());
				punchService.refreshPunchDayLog(request.getUserId(), request.getEnterpriseId(), startDay, endDay);
			}
		}catch(Exception e){
			LOGGER.error("flow case create refreshPunchDayLog error ", e);
		}
	}

	private void addVacationBalanceHistoryCount(Long userId, GeneralApproval ga, PunchExceptionRequest request, byte operation) {
		if (!GeneralApprovalAttribute.ASK_FOR_LEAVE.getCode().equals(ga.getApprovalAttribute())
				|| request.getCategoryId() == null || request.getCategoryId() <= 0) {
			return;
		}
		ApprovalCategory approvalCategory = approvalCategoryProvider.findApprovalCategoryById(request.getCategoryId());
		if (approvalCategory == null) {
			return;
		}
		if ("ANNUAL_LEAVE".equals(approvalCategory.getHanderType())) {
			punchVacationBalanceService.addVacationBalanceHistoryCount(userId, ga.getOrganizationId(), request.getDurationDay().multiply(BigDecimal.valueOf(operation)), BigDecimal.ZERO);
		} else if ("WORKING_DAY_OFF".equals(approvalCategory.getHanderType())) {
			punchVacationBalanceService.addVacationBalanceHistoryCount(userId, ga.getOrganizationId(), BigDecimal.ZERO, request.getDurationDay().multiply(BigDecimal.valueOf(operation)));
		}
	}

	private void updateVacationBalance(Long applyUserId, GeneralApproval ga, PunchExceptionRequest request, byte operation, String description) {
		if (!GeneralApprovalAttribute.ASK_FOR_LEAVE.getCode().equals(ga.getApprovalAttribute())
				|| request.getCategoryId() == null || request.getCategoryId() <= 0) {
			return;
		}
		ApprovalCategory approvalCategory = approvalCategoryProvider.findApprovalCategoryById(request.getCategoryId());
		if (approvalCategory == null || approvalCategory.getNamespaceId() == null || approvalCategory.getNamespaceId() == 0) {
			// 旧数据不更新余额
			return;
		}
		OrganizationMemberDetails organizationMemberDetail = organizationProvider.findOrganizationMemberDetailsByTargetIdAndOrgId(applyUserId, ga.getOrganizationId());
		UpdateVacationBalancesCommand updateVacationBalancesCommand = new UpdateVacationBalancesCommand();
		updateVacationBalancesCommand.setOrganizationId(ga.getOrganizationId());
		updateVacationBalancesCommand.setDetailId(organizationMemberDetail.getId());
		updateVacationBalancesCommand.setDescription(description);
		int notificationCode = operation < 0 ? ApprovalServiceConstants.VACATION_BALANCE_CHANGED_NOTIFICATION_CONTENT_BY_REQUEST_SUBMIT : ApprovalServiceConstants.VACATION_BALANCE_CHANGED_NOTIFICATION_CONTENT_BY_REQUEST_CANCELD;
		if ("ANNUAL_LEAVE".equals(approvalCategory.getHanderType())) {
			updateVacationBalancesCommand.setAnnualLeaveBalanceCorrection(request.getDurationDay().doubleValue() * operation);
			PunchVacationBalance punchVacationBalance = punchVacationBalanceService.updateVacationBalances(updateVacationBalancesCommand);
			String notificationContent = punchVacationBalanceService.buildVacationBalanceChangedNotificationContent(updateVacationBalancesCommand.getAnnualLeaveBalanceCorrection(),
					0D, request, punchVacationBalance, approvalCategory, notificationCode);
			punchVacationBalanceService.sendMessageWhenVacationBalanceChanged(organizationMemberDetail, notificationContent);
		} else if ("WORKING_DAY_OFF".equals(approvalCategory.getHanderType())) {
			updateVacationBalancesCommand.setOvertimeCompensationBalanceCorrection(request.getDurationDay().doubleValue() * operation);
			PunchVacationBalance punchVacationBalance = punchVacationBalanceService.updateVacationBalances(updateVacationBalancesCommand);
			String notificationContent = punchVacationBalanceService.buildVacationBalanceChangedNotificationContent(0D,
					updateVacationBalancesCommand.getOvertimeCompensationBalanceCorrection(), request, punchVacationBalance, approvalCategory, notificationCode);
			punchVacationBalanceService.sendMessageWhenVacationBalanceChanged(organizationMemberDetail, notificationContent);
		}
	}

	private Timestamp parseStartTime(Long organizationId, Long applyUserId, String startTimeStr) {
		if (startTimeStr.contains("上半天")) {
			Date date = punchService.parseDateTimeByTimeSelectType(organizationId, applyUserId, startTimeStr.replace("上半天", "").trim(), ApprovalCategoryTimeSelectType.FIRST_HALF_BEGIN);
			return new Timestamp(date.getTime());
		}
		if (startTimeStr.contains("下半天")) {
			Date date = punchService.parseDateTimeByTimeSelectType(organizationId, applyUserId, startTimeStr.replace("下半天", "").trim(), ApprovalCategoryTimeSelectType.FIRST_HALF_END);
			return new Timestamp(date.getTime());
		}
		return Timestamp.valueOf(startTimeStr + ":00");
	}

	private Timestamp parseEndTime(Long organizationId, Long applyUserId, String endTimeStr) {
		if (endTimeStr.contains("上半天")) {
			Date date = punchService.parseDateTimeByTimeSelectType(organizationId, applyUserId, endTimeStr.replace("上半天", "").trim(), ApprovalCategoryTimeSelectType.SECOND_HALF_BEGIN);
			return new Timestamp(date.getTime());
		}
		if (endTimeStr.contains("下半天")) {
			Date date = punchService.parseDateTimeByTimeSelectType(organizationId, applyUserId, endTimeStr.replace("下半天", "").trim(), ApprovalCategoryTimeSelectType.SECOND_HALF_END);
			return new Timestamp(date.getTime());
		}
		return Timestamp.valueOf(endTimeStr + ":00");
	}

}
