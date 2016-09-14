package com.everhomes.approval;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.approval.AbsenceBasicDescription;
import com.everhomes.rest.approval.ApprovalBasicInfoOfRequestDTO;
import com.everhomes.rest.approval.ApprovalOwnerInfo;
import com.everhomes.rest.approval.ApprovalTypeTemplateCode;
import com.everhomes.rest.approval.BriefApprovalRequestDTO;
import com.everhomes.rest.approval.CreateApprovalRequestBySceneCommand;
import com.everhomes.rest.approval.TimeRange;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.techpark.punch.PunchRule;
import com.everhomes.techpark.punch.PunchService;
import com.everhomes.techpark.punch.PunchTimeRule;
import com.everhomes.util.DateHelper;
import com.everhomes.util.ListUtils;
import com.everhomes.util.RuntimeErrorException;

/**
 * 
 * <ul>
 * 用于审批的处理请假的handler
 * </ul>
 */
@Component(ApprovalRequestHandler.APPROVAL_REQUEST_OBJECT_PREFIX + ApprovalTypeTemplateCode.ABSENCE)
public class ApprovalRequestAbsenceHandler extends ApprovalRequestDefaultHandler {
	//时间相加减需要带上这个差值
	private static final Long MILLISECONDGMT=8*3600*1000L;
	private static final SimpleDateFormat timeSF = new SimpleDateFormat("HH:mm:ss");
	private static final SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat monthSF = new SimpleDateFormat("yyyyMM");
	private static final SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private ApprovalCategoryProvider approvalCategoryProvider;
	
	@Autowired
	private ApprovalService approvalService;
	
	@Autowired
	private ApprovalTimeRangeProvider approvalTimeRangeProvider;
	
	@Autowired
	private PunchService punchService;
	
	@Override
	public ApprovalBasicInfoOfRequestDTO processApprovalBasicInfoOfRequest(ApprovalRequest approvalRequest) {
		ApprovalBasicInfoOfRequestDTO approvalBasicInfo = super.processApprovalBasicInfoOfRequest(approvalRequest);
		
		AbsenceBasicDescription description = new AbsenceBasicDescription();
		
		//1. 获取类别名称
		ApprovalCategory approvalCategory = approvalCategoryProvider.findApprovalCategoryById(approvalRequest.getCategoryId());
		if (approvalCategory != null) {
			description.setCategoryName(approvalCategory.getCategoryName());
		}
		
		//2. 获取时间
		if (approvalRequest.getTimeFlag().byteValue() == TrueOrFalseFlag.TRUE.getCode()) {
			List<TimeRange> timeRangeList = approvalService.listTimeRangeByRequestId(approvalRequest.getId());
			timeRangeList.forEach(t->{
				description.setTimeTotal(calculateTimeTotal(description.getTimeTotal(), t.getActualResult()));
			});
			description.setTimeRangeList(timeRangeList);
		}
		
		approvalBasicInfo.setDescriptionJson(JSON.toJSONString(description));
		return approvalBasicInfo;
	}

	
	@Override
	public BriefApprovalRequestDTO processBriefApprovalRequest(ApprovalRequest approvalRequest) {
		BriefApprovalRequestDTO briefApprovalRequestDTO = super.processBriefApprovalRequest(approvalRequest);
		String timeTotal = null;
		if (approvalRequest.getTimeFlag().byteValue() == TrueOrFalseFlag.TRUE.getCode()) {
			List<TimeRange> timeRangeList = briefApprovalRequestDTO.getTimeRangeList();
			for (TimeRange timeRange : timeRangeList) {
				timeTotal = calculateTimeTotal(timeTotal, timeRange.getActualResult());
			}
		}
		briefApprovalRequestDTO.setDescription(timeTotal);
		briefApprovalRequestDTO.setReason(JSONObject.parseObject(approvalRequest.getContentJson()).getString("reason"));
		
		return briefApprovalRequestDTO;
	}

	private String calculateTimeTotal(String timeTotal, String actualResult) {
		//表中按1.25.33这样存储，每一位分别代表天、小时、分钟，统计时需要每个位分别相加，且小时满24不用进一，分钟满60需要进一，如果某一位是0也必须存储，也就是说结果中必须包含两个小数点
		if (StringUtils.isBlank(timeTotal)) {
			return actualResult;
		}
		
		String[] times = timeTotal.split(".");
		String[] actuals = actualResult.split(".");
		
		int days = Integer.parseInt(times[0]) + Integer.parseInt(actuals[0]);
		int hours = Integer.parseInt(times[1]) + Integer.parseInt(actuals[1]);
		int minutes = Integer.parseInt(times[2]) + Integer.parseInt(actuals[2]);
		
		hours = hours + minutes / 60;
		minutes = minutes % 60;
		
		return days + "." + hours + "." + minutes;
	}


	@Override
	public ApprovalRequest preProcessCreateApprovalRequest(Long userId, ApprovalOwnerInfo ownerInfo,
			CreateApprovalRequestBySceneCommand cmd) {
		//请假理由不能为空
		if (StringUtils.isBlank(cmd.getReason())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"reason cannot be empty");
		}
		//请假时间不能为空
		if (ListUtils.isEmpty(cmd.getTimeRangeList()) || checkTimeEmpty(cmd.getTimeRangeList())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"time cannot be empty");
		}
		//开始时间不能大于等于结束时间
		if (checkTimeFromGreaterThanEnd(cmd.getTimeRangeList())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"from time cannot be greater than end time");
		}
		PunchRule punchRule = punchService.getPunchRule(ownerInfo.getOwnerType(), ownerInfo.getOwnerId(), userId);
		//请假时间不能在非工作时间
		if (checkNotInWorkTime(cmd.getTimeRangeList(), punchRule)) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"absence time not in work time");
		}
		//请假时间不能包含已请假时间
		if (checkContainRequestedTime(userId, ownerInfo, cmd.getTimeRangeList())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"absence time cannot contain requested time");
		}
		calculateActualResult(userId, punchRule, cmd.getTimeRangeList());
		
		
		return super.preProcessCreateApprovalRequest(userId, ownerInfo, cmd);
	}

	private boolean checkNotInWorkTime(List<TimeRange> timeRangeList, PunchRule punchRule) {
		for (TimeRange timeRange : timeRangeList) {
			if (!punchService.isWorkDay(new Date(timeRange.getFromTime()), punchRule)
					|| !punchService.isWorkDay(new Date(timeRange.getEndTime()), punchRule)
					|| !punchService.isWorkTime(new Time(timeRange.getFromTime()), punchRule)
					|| !punchService.isWorkTime(new Time(timeRange.getEndTime()), punchRule)
					|| !punchService.isRestTime(new Date(timeRange.getFromTime()), new Date(timeRange.getEndTime()), punchRule)) {
				return true;
			}
		}
		return false;
	}

	//检查是否包含已请假时间
	//timeRangeList为传输对象，approvalTimeRangeList为数据库表对应的对象
	private boolean checkContainRequestedTime(Long userId, ApprovalOwnerInfo ownerInfo, List<TimeRange> timeRangeList) {
		//查询当前用户的请假记录，再一个一个比较时间是否有重叠
		List<ApprovalTimeRange> requestedTimeRangeList = approvalTimeRangeProvider.listApprovalTimeRangeByUserId(userId, ownerInfo.getNamespaceId(), ownerInfo.getOwnerType(), ownerInfo.getOwnerId());
		if (ListUtils.isNotEmpty(requestedTimeRangeList)) {
			for (TimeRange timeRange : timeRangeList) {
				for (ApprovalTimeRange requestedTimeRange : requestedTimeRangeList) {
					if (timeRange.getFromTime() == requestedTimeRange.getFromTime().getTime() && timeRange.getEndTime() == requestedTimeRange.getEndTime().getTime()) {
						return true;
					}else if (timeRange.getFromTime() >= requestedTimeRange.getFromTime().getTime() && timeRange.getFromTime() < requestedTimeRange.getEndTime().getTime()) {
						return true;
					}else if (timeRange.getEndTime() <= requestedTimeRange.getEndTime().getTime() && timeRange.getEndTime() > requestedTimeRange.getFromTime().getTime()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private void convertTimeRangeList(List<TimeRange> timeRangeList, List<ApprovalTimeRange> approvalTimeRangeList, Long userId) {
		approvalTimeRangeList.addAll(timeRangeList.stream().map(t->{
			ApprovalTimeRange approvalTimeRange = new ApprovalTimeRange();
			approvalTimeRange.setFromTime(new Timestamp(t.getFromTime()));
			approvalTimeRange.setEndTime(new Timestamp(t.getEndTime()));
			approvalTimeRange.setType(t.getType());
			approvalTimeRange.setActualResult(null);
			approvalTimeRange.setCreatorUid(userId);
			approvalTimeRange.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			return approvalTimeRange;
		}).collect(Collectors.toList()));
	}


	private String calculateActualResult(Long userId, PunchRule punchRule, List<TimeRange> timeRangeList) {
		PunchTimeRule punchTimeRule = punchService.getPunchTimeRule(punchRule);
		timeRangeList.forEach(a->{
			//如果不跨天
			if (punchService.isSameDay(new Date(a.getFromTime()), new Date(a.getEndTime()))) {
				Time fromTime = Time.valueOf(timeSF.format(new Date(a.getFromTime())));
				//如果请假开始时间小于最晚上班时间，以最晚上班时间为准
				if (fromTime.getTime() < punchTimeRule.getStartLateTime().getTime()) {
					fromTime = punchTimeRule.getStartLateTime();
				}
				
				
				
				
				
				
				
				
				
				
			}
			
			
			
			
			
			
		});
		
		
		
		
		
		return null;
	}
	
	private boolean checkTimeFromGreaterThanEnd(List<TimeRange> timeRangeList) {
		for (TimeRange timeRange : timeRangeList) {
			if (timeRange.getFromTime().longValue() >= timeRange.getEndTime().longValue()) {
				return true;
			}
		}
		return false;
	}


	private boolean checkTimeEmpty(List<TimeRange> timeRangeList) {
		for (TimeRange timeRange : timeRangeList) {
			if (timeRange.getFromTime() == null || timeRange.getEndTime() == null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void postProcessCreateApprovalRequest(Long userId, ApprovalOwnerInfo ownerInfo, ApprovalRequest approvalRequest,
			CreateApprovalRequestBySceneCommand cmd) {
		//添加请假时间
//		createTimeRange(userId, ownerInfo, approvalRequest.getId(), approvalTimeRangeList);
	}

	//实际请假时间及时长放在前置处理器中计算
	private void createTimeRange(Long userId, ApprovalOwnerInfo ownerInfo, Long requestId) {
//		approvalTimeRangeList.forEach(a->a.setOwnerId(requestId));
//		approvalTimeRangeProvider.createApprovalTimeRanges(approvalTimeRangeList);
	}

}
