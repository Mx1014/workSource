package com.everhomes.approval;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors; 

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component; 




















import javassist.runtime.DotClass;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.joda.time.Hours;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;





















import ch.qos.logback.classic.Logger;
 




import com.everhomes.constants.ErrorCodes;
import com.everhomes.flow.FlowCase;
import com.everhomes.rentalv2.RentalNotificationTemplateCode;
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
import com.everhomes.rest.approval.TimeRange;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseEntityType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.techpark.punch.PunchTimeRuleDTO;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.techpark.punch.PunchConstants;
import com.everhomes.techpark.punch.PunchRule;
import com.everhomes.techpark.punch.PunchService;
import com.everhomes.techpark.punch.PunchTimeRule;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
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
	private static final Long DAY_MILLISECONDGMT=24*3600*1000L;

	
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ApprovalRequestDefaultHandler.class);
//	private static final SimpleDateFormat timeSF = new SimpleDateFormat("HH:mm:ss");
//	private static final SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
//	private static final SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	private static final SimpleDateFormat dateMinutesSF = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static ThreadLocal<SimpleDateFormat> dateSF = new ThreadLocal<SimpleDateFormat>(){
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    }; 
    private static ThreadLocal<SimpleDateFormat> timeSF = new ThreadLocal<SimpleDateFormat>(){
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm:ss");
        }
    }; 
    private static ThreadLocal<SimpleDateFormat> monthSF = new ThreadLocal<SimpleDateFormat>(){
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMM");
        }
    }; 
    private static ThreadLocal<SimpleDateFormat> datetimeSF = new ThreadLocal<SimpleDateFormat>(){
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    private static ThreadLocal<SimpleDateFormat> dateMinutesSF = new ThreadLocal<SimpleDateFormat>(){
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }
    };
	@Autowired
	private ApprovalCategoryProvider approvalCategoryProvider;
	
	@Autowired
	private ApprovalService approvalService;
	
	@Autowired
	private ApprovalTimeRangeProvider approvalTimeRangeProvider;
	
	@Autowired
	private PunchService punchService;
	
	@Autowired
	private ApprovalRangeStatisticProvider approvalRangeStatisticProvider;

	@Autowired
	private ApprovalDayActualTimeProvider approvalDayActualTimeProvider;
	
	
	@Override
	public ApprovalBasicInfoOfRequestDTO processApprovalBasicInfoOfRequest(ApprovalRequest approvalRequest) {
		ApprovalBasicInfoOfRequestDTO approvalBasicInfo = super.processApprovalBasicInfoOfRequest(approvalRequest);
		
		BasicDescriptionDTO description = new BasicDescriptionDTO();
		
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
		
		approvalBasicInfo.setDescriptionJson(description.toString());
		return approvalBasicInfo;
	}

	
	@Override
	public BriefApprovalRequestDTO processBriefApprovalRequest(ApprovalRequest approvalRequest) {
		BriefApprovalRequestDTO briefApprovalRequestDTO = super.processBriefApprovalRequest(approvalRequest);
		String timeTotal = null;

		Long fromTime = null;
		Long endTime =null ;
		if (approvalRequest.getTimeFlag().byteValue() == TrueOrFalseFlag.TRUE.getCode()) {
			List<TimeRange> timeRangeList = briefApprovalRequestDTO.getTimeRangeList();
			for (TimeRange timeRange : timeRangeList) {
				timeTotal = calculateTimeTotal(timeTotal, timeRange.getActualResult());
				if(null == fromTime || fromTime > timeRange.getFromTime())
					fromTime = timeRange.getFromTime();
				if(null == endTime || endTime < timeRange.getEndTime())
					endTime = timeRange.getEndTime();
			}
		}
		briefApprovalRequestDTO.setDescription(timeTotal);
		briefApprovalRequestDTO.setTitle(processBriefRequestTitle(approvalRequest ,timeTotal,fromTime,endTime));
		return briefApprovalRequestDTO;
	}
	

	private String calculateTimeTotal(String timeTotal, String actualResult) {
		//表中按1.25.33这样存储，每一位分别代表天、小时、分钟，统计时需要每个位分别相加，且小时满24不用进一，分钟满60需要进一，如果某一位是0也必须存储，也就是说结果中必须包含两个小数点
		if (StringUtils.isBlank(timeTotal)) {
			return actualResult;
		}
		
		String[] times = timeTotal.split("\\.");
		String[] actuals = actualResult.split("\\.");
		
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
		if (cmd.getCategoryId() == null) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE, ApprovalServiceErrorCode.ABSENCE_EMPTY_CATEGORY,
					"category cannot be empty");
		}
		//请假理由不能为空
		if (StringUtils.isBlank(cmd.getReason())) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE, ApprovalServiceErrorCode.ABSENCE_EMPTY_REASON,
					"reason cannot be empty");
		}
		//请假时间不能为空
		if (ListUtils.isEmpty(cmd.getTimeRangeList()) || checkTimeEmpty(cmd.getTimeRangeList())) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE, ApprovalServiceErrorCode.ABSENCE_EMPTY_TIME,
					"time cannot be empty");
		}
		//开始时间不能大于等于结束时间
		if (checkTimeFromGreaterThanEnd(cmd.getTimeRangeList())) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE, ApprovalServiceErrorCode.ABSENCE_FROM_TIME_MUST_LT_END_TIME,
					"from time cannot be greater than end time");
		}
		PunchRule punchRule = punchService.getPunchRule(ownerInfo.getOwnerType(), ownerInfo.getOwnerId(), userId);
		//请假时间不能在非工作时间
		if (checkNotInWorkTime(cmd.getTimeRangeList(), punchRule)) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE, ApprovalServiceErrorCode.ABSENCE_NOT_WORK_TIME,
					"absence time not in work time");
		}
		//请假时间不能包含已请假时间
		if (checkContainRequestedTime(userId, ownerInfo, cmd.getTimeRangeList())) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE, ApprovalServiceErrorCode.ABSENCE_TIME_CONTAINS_REQUESTED_TIME,
					"absence time cannot contain requested time");
		}
		List<ApprovalDayActualTime> approvalDayActualTimeList = calculateActualResult(userId, punchRule, cmd.getTimeRangeList());
		
		ApprovalRequest approvalRequest = super.preProcessCreateApprovalRequest(userId, ownerInfo, cmd);
		approvalRequest.setApprovalDayActualTimeList(approvalDayActualTimeList);
		return approvalRequest;
	}
	private boolean checkNotInWorkTime(List<TimeRange> timeRangeList, PunchRule punchRule) {
//		for (TimeRange timeRange : timeRangeList) {
//			if (!isWorkDay(new Date(timeRange.getFromTime()), punchRule)
//					|| !isWorkDay(new Date(timeRange.getEndTime()), punchRule)
//					|| !punchService.isWorkTime(new Time(timeRange.getFromTime()), punchRule,new Date(timeRange.getFromTime()))
//					|| !punchService.isWorkTime(new Time(timeRange.getEndTime()), punchRule,new Date(timeRange.getEndTime()))
//					|| punchService.isRestTime(new Date(timeRange.getFromTime()), new Date(timeRange.getEndTime()), punchRule)) {
//				return true;
//			}
//		}
		return false;
	}

	private boolean isWorkDay(Date date, PunchRule punchRule) {
		PunchTimeRuleDTO dto = processTimeRuleDTO(punchRule.getId(), date);
		if(dto != null)
			return true;
		Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(date);
		PunchTimeRuleDTO yesterdayRuleDTO = processTimeRuleDTO(punchRule.getId(), yesterday.getTime()); 
		if(null != yesterdayRuleDTO && yesterdayRuleDTO.getEndEarlyTime() > DAY_MILLISECONDGMT)
			return true;
		return false;
	}


	//检查是否包含已请假时间
	//timeRangeList为传输对象，approvalTimeRangeList为数据库表对应的对象
	private boolean checkContainRequestedTime(Long userId, ApprovalOwnerInfo ownerInfo, List<TimeRange> timeRangeList) {
		//查询当前用户的请假记录，再一个一个比较时间是否有重叠
		List<ApprovalTimeRange> requestedTimeRangeList = approvalTimeRangeProvider.listApprovalTimeRangeByUserIdForCheckDuplicatedTime(userId, ownerInfo.getNamespaceId(), ownerInfo.getOwnerType(), ownerInfo.getOwnerId());
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

	private List<ApprovalDayActualTime> calculateActualResult(Long userId, PunchRule punchRule, List<TimeRange> timeRangeList) {
		List<ApprovalDayActualTime> approvalDayActualTimeList = new ArrayList<>();
		timeRangeList.forEach(a->{
			//如果不跨天
			if (punchService.isSameDay(new Date(a.getFromTime()), new Date(a.getEndTime()))) {
				//1.用昨天的规则计算一下时间 注意:起止时间要加一天的毫秒数
				Calendar yestCalendar = Calendar.getInstance();
				yestCalendar.setTimeInMillis(a.getFromTime()); 
				yestCalendar.add(Calendar.DAY_OF_MONTH, -1);
				PunchTimeRuleDTO dto = processTimeRuleDTO(punchRule.getId(), yestCalendar.getTime()) ;
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(a.getFromTime());
				long fromTime = calendar.get(Calendar.HOUR_OF_DAY)*3600*1000L +calendar.get(Calendar.MINUTE)*60*1000L +calendar.get(Calendar.SECOND)*1000L;
				calendar.setTimeInMillis(a.getEndTime());
				long endTime = calendar.get(Calendar.HOUR_OF_DAY)*3600*1000L +calendar.get(Calendar.MINUTE)*60*1000L +calendar.get(Calendar.SECOND)*1000L; 
				long yesterdayTime = calculateOneDayTime( fromTime+ DAY_MILLISECONDGMT, endTime  + DAY_MILLISECONDGMT, dto);
				//2.用今天的规则计算一下时间
				Calendar todayCalendar = Calendar.getInstance();
				todayCalendar.setTimeInMillis(a.getFromTime());  
				dto = processTimeRuleDTO(punchRule.getId(), todayCalendar.getTime()) ;
				long todayTime = calculateOneDayTime( fromTime,endTime, dto);
				//3.把用昨天规则计算的和今天规则计算的加起来
				ApprovalDayActualTime actualTime = new ApprovalDayActualTime();
				actualTime.setUserId(userId);
				actualTime.setTimeDate(new java.sql.Date(a.getFromTime()));
				MyDate myDate = new MyDate();
				caculateAbsentDate(myDate, yesterdayTime+todayTime);
				actualTime.setActualResult(myDate.toString());
				a.setActualResult(myDate.toString());
				approvalDayActualTimeList.add(actualTime);
			} else {
//				//如果跨天
//				//(一)直接计算天数，起止时间分别在最晚上班时间之前，起止时间分别在最早下班之后，开始时间在最晚上班之前且结束时间在最早下班时间之后，开始时间在最早下班时间之后且结束时间在最晚上班时间之前
				Date fromTime = new Date(a.getFromTime());
				PunchTimeRule fromPunchTimeRule = punchService.getPunchTimeRuleByRuleIdAndDate(punchRule.getId(), fromTime) ;
				Date endTime = new Date(a.getEndTime());
				PunchTimeRule endPunchTimeRule = punchService.getPunchTimeRuleByRuleIdAndDate(punchRule.getId(), endTime) ;
//				Time endEarlyTime = punchService.getEndTime(endPunchTimeRule.getStartEarlyTime(), endPunchTimeRule.getWorkTime());
//				if ((!Time.valueOf(timeSF.get().format(fromTime)).after(fromPunchTimeRule.getStartLateTime()) && !Time.valueOf(timeSF.get().format(endTime)).after(endPunchTimeRule.getStartLateTime()))) {
//					//起止时间分别在最晚上班时间之前
//					a.setActualResult(convertDayToActualResult(calculateDeltaDay(fromTime, endTime) - calculateNotWorkDay(fromTime, endTime, userId, punchRule)));
//					calculateEverydayResultWholeDay(userId, punchRule, fromTime, endTime, true, false, approvalDayActualTimeList);
//				}else if ((!Time.valueOf(timeSF.get().format(fromTime)).before(endEarlyTime) && !Time.valueOf(timeSF.get().format(endTime)).before(endEarlyTime))) {
//					//起止时间分别在最早下班之后
//					a.setActualResult(convertDayToActualResult(calculateDeltaDay(fromTime, endTime) - calculateNotWorkDay(fromTime, endTime, userId, punchRule)));
//					calculateEverydayResultWholeDay(userId, punchRule, fromTime, endTime, false, true, approvalDayActualTimeList);
//				}else if (!Time.valueOf(timeSF.get().format(fromTime)).after(fromPunchTimeRule.getStartLateTime()) && !Time.valueOf(timeSF.get().format(endTime)).before(endEarlyTime)) {
//					//开始时间在最晚上班之前且结束时间在最早下班时间之后
//					a.setActualResult(convertDayToActualResult(calculateDeltaDay(fromTime, endTime) + 1 - calculateNotWorkDay(fromTime, endTime, userId, punchRule)));
//					calculateEverydayResultWholeDay(userId, punchRule, fromTime, endTime, true, true, approvalDayActualTimeList);
//				}else if (!Time.valueOf(timeSF.get().format(fromTime)).before(endEarlyTime) && !Time.valueOf(timeSF.get().format(endTime)).after(endPunchTimeRule.getStartLateTime())) {
//					//开始时间在最早下班时间之后且结束时间在最晚上班时间之前
//					a.setActualResult(convertDayToActualResult(calculateDeltaDay(fromTime, endTime) - 1 - calculateNotWorkDay(fromTime, endTime, userId, punchRule)));
//					calculateEverydayResultWholeDay(userId, punchRule, fromTime, endTime, false, false, approvalDayActualTimeList);
//				}else {
					//(二)不直接计算天数
					//0. 计算每天的实际时长
//					calculateEverydayResultTime(userId, punchRule, fromPunchTimeRule,endPunchTimeRule, fromTime, endTime, approvalDayActualTimeList);
					//1. 格式化起止时间，如果小于最晚上班时间，则以最晚上班时间计算，如果大于最早下班时间，则以最早下班时间计算，分别记为fromTime, endTime
//					fromTime = formatTime(a.getFromTime(), fromPunchTimeRule.getStartLateTime(), punchService.getEndTime(fromPunchTimeRule.getStartEarlyTime(), fromPunchTimeRule.getWorkTime())); //formatFromTime(a.getFromTime(), punchTimeRule.getStartLateTime());
//					endTime = formatTime(a.getEndTime(), endPunchTimeRule.getStartLateTime(), punchService.getEndTime(endPunchTimeRule.getStartEarlyTime(), endPunchTimeRule.getWorkTime())); //formatEndTime(a.getEndTime(), punchService.getEndTime(punchTimeRule.getStartEarlyTime(), punchTimeRule.getWorkTime()));
//					deleted by wh. 这段已经不用了,但为了祭奠唐彤费尽脑汁的贡献代码,我决定留着供后人参详
//					//2. 计算午休时间差, 记为deltaNoonRestTime
//					long deltaNoonRestTime = punchTimeRule.getAfternoonArriveTime().getTime() - punchTimeRule.getNoonLeaveTime().getTime();
//					//3. 计算最早下班时间与最晚上班时间的时间差并扣除午休时间，记为deltaWorkTime
//					long deltaWorkTime = punchService.getEndTime(punchTimeRule.getStartEarlyTime(), punchTimeRule.getWorkTime()).getTime() - punchTimeRule.getStartLateTime().getTime() - (deltaNoonRestTime);
//					//4. 计算最早下班时间到第二天最晚上班时间之间的时间差，记为deltaAcrossDayTime
//					long deltaAcrossDayTime = DAY_MILLISECONDGMT - (punchService.getEndTime(punchTimeRule.getStartEarlyTime(), punchTimeRule.getWorkTime()).getTime() - punchTimeRule.getStartLateTime().getTime());
//					//5. 计算跨越的天数，即起止日期相减的天数，记为deltaDay
//					long deltaDay = calculateDeltaDay(fromTime, endTime);
//					//6. 计算午休时间次数，如果同在午休开始时间之前或同在午休结束时间之后，则与跨越天数相同，如果在正向两侧则为跨越天数+1，如果在反向两侧则为跨越开数-1，记为noonRestTimes
//					long noonRestTimes = 0L;
//					if ((!Time.valueOf(timeSF.get().format(fromTime)).after(punchTimeRule.getNoonLeaveTime()) && !Time.valueOf(timeSF.get().format(endTime)).after(punchTimeRule.getNoonLeaveTime()))
//							|| (!Time.valueOf(timeSF.get().format(fromTime)).before(punchTimeRule.getAfternoonArriveTime()) && !Time.valueOf(timeSF.get().format(endTime)).before(punchTimeRule.getAfternoonArriveTime()))) {
//						noonRestTimes = deltaDay;
//					}else if (!Time.valueOf(timeSF.get().format(fromTime)).after(punchTimeRule.getNoonLeaveTime()) && !Time.valueOf(timeSF.get().format(endTime)).before(punchTimeRule.getAfternoonArriveTime())) {
//						noonRestTimes = deltaDay + 1;
//					}else if (!Time.valueOf(timeSF.get().format(fromTime)).before(punchTimeRule.getAfternoonArriveTime()) && !Time.valueOf(timeSF.get().format(endTime)).after(punchTimeRule.getNoonLeaveTime())) {
//						noonRestTimes = deltaDay - 1;
//					}
//					//7. 计算非工作日的天数，记为deltaNotWorkDay
//					long deltaNotWorkDay = calculateNotWorkDay(fromTime, endTime, userId, punchRule);
//					//8. 得出公式，(endTime-fromTime)-deltaDay*deltaAcrossDayTime-noonRestTimes*deltaNoonRestTime-deltaNotWorkDay*deltaWorkTime=最终请假的时间，记为finalAbsentTime
//					long finalAbsentTime = (endTime.getTime() - fromTime.getTime()) - deltaDay * deltaAcrossDayTime - noonRestTimes * deltaNoonRestTime - deltaNotWorkDay * deltaWorkTime;
//					//9. 如果以上最终请假时间大于（不包含）deltaWorkTime，则finalAbsentTime/deltaWorkTime的结果即为天数，余数即为小时分钟数
					MyDate myDate = processMyDate(fromTime,endTime,punchRule);
					a.setActualResult(myDate.toString());
					
				}
//			}
			//产品的文档上是可以存在请假时长为0的
//			if (a.getActualResult().equals(new MyDate().toString())) {
//				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//						"no actual absent time");
//			}
			
		});
		return approvalDayActualTimeList;
	}

	private long calculateOneDayTime(long fromTime, long endTime, PunchTimeRuleDTO dto) {
		//如果fromTime超过了最早下班时间或者endTime早于最早上班时间,return 0
 		LOGGER.debug("fromtime ="+fromTime+",endtime ="+endTime+",dto = "+dto);
		if(fromTime > dto.getEndEarlyTime() || endTime < dto.getStartEarlyTime())
			return 0L;
		// 确定起始时间:
		//1.如果fromTime在一天最早上班时间之前,则用最早上班时间
		//如果fromTime 在午休时间之内 则用午休结束时间
		if(fromTime < dto.getStartEarlyTime())
			fromTime = dto.getStartEarlyTime();
		else if((fromTime > dto.getNoonLeaveTime()) && (fromTime < dto.getAfternoonArriveTime()))
			fromTime = dto.getAfternoonArriveTime();
		//2.如果endTime在最早下班时间之后则用最早下班时间,否则用endTime
		//如果endTime在午休时间之内则用午休开始时间
		if(endTime > dto.getEndEarlyTime())
			endTime = dto.getEndEarlyTime();
		else if((endTime > dto.getNoonLeaveTime()) && (endTime < dto.getAfternoonArriveTime()))
			endTime = dto.getNoonLeaveTime();
		//3. 两者相减计算请假时间:如果小于0则设置为0
		long deltaMs = endTime - fromTime;
		if(deltaMs < 0)
			return 0L;
		//3.如果中间有午休(开始时间小于午休开始时间,结束时间大于午休结束时间)减去午休
		if ( (fromTime < dto.getNoonLeaveTime()) && (endTime > dto.getAfternoonArriveTime())){
			deltaMs = deltaMs - (dto.getAfternoonArriveTime() -dto.getNoonLeaveTime() );
		}
			
		return deltaMs;
	}


	private MyDate processMyDate(Date fromTime, Date endTime, PunchRule punchRule) {
		MyDate myDate = new MyDate(); 
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(fromTime);
		startCalendar.set(Calendar.HOUR_OF_DAY, 1);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endTime);
		endCalendar.set(Calendar.HOUR_OF_DAY, 2);
		//循环从请假开始日到结束日
		for(;startCalendar.before(endCalendar);startCalendar.add(Calendar.DAY_OF_MONTH, 1)){
			Calendar yestCalendar = Calendar.getInstance();
			yestCalendar.setTime(startCalendar.getTime()); 
			PunchTimeRuleDTO dto = processTimeRuleDTO(punchRule.getId(), yestCalendar.getTime()) ;
			//如果是开始日 
			if(dateSF.get().format(startCalendar.getTime()).equals(dateSF.get().format(fromTime))){
				//计算前一天的
				//拿前一天的timerule判断是否有班次.班次的结束时间>请假开始时间?日期+1:不变
				if(dto != null ){
					//拿前一天的timerule进行计算
					Long fromTimeLong = punchService.convertTimeToGMTMillisecond(new Time(fromTime.getTime()))+DAY_MILLISECONDGMT; 
					calculateAbsentBeginDate(myDate, dto, fromTimeLong);					
				} 
				//计算当日的   
				dto = processTimeRuleDTO(punchRule.getId(), startCalendar.getTime()) ; 
				if(dto != null ){
					//拿前一天的timerule进行计算,
					Long fromTimeLong = punchService.convertTimeToGMTMillisecond(new Time(fromTime.getTime())); 
					calculateAbsentBeginDate(myDate, dto, fromTimeLong);					
				}
			}
			//如果是结束日			
			else if(dateSF.get().format(startCalendar.getTime()).equals(dateSF.get().format(endTime))){
				//拿前一天的timerule判断是否有班次.
				if(dto != null ){
					//拿前一天的timerule进行计算 
					//前一天班次的最晚下班时间>请假开始时间?日期-1:不变 
					Long endTimeLong = punchService.convertTimeToGMTMillisecond(new Time(endTime.getTime()))+DAY_MILLISECONDGMT; 
					if(dto.getEndEarlyTime()>endTimeLong){
						int days = myDate.getDays()-1;
						myDate.setDays(days);
						calculateAbsentEndDate(myDate, dto, endTimeLong);					
					}
					
				} 
				//计算结束日当天的  
				dto = processTimeRuleDTO(punchRule.getId(), startCalendar.getTime()) ; 
				if(dto != null ){
					//拿前一天的timerule进行计算,
					Long endTimeLong = punchService.convertTimeToGMTMillisecond(new Time(endTime.getTime())); 
					calculateAbsentEndDate(myDate, dto, endTimeLong);					
				}
			}else{
				//如果非开始日和结束日
				//判断是否有班次,有则日期+1
				dto = processTimeRuleDTO(punchRule.getId(), startCalendar.getTime()) ; 
				if(null != dto){
					int days = myDate.getDays()+1;
					myDate.setDays(days);
				}
			}
		}
		return myDate;
	}

	private PunchTimeRuleDTO processTimeRuleDTO(Long punchRuleId  ,Date date) {
		PunchTimeRule dayPunchTimeRule = punchService.getPunchTimeRuleByRuleIdAndDate(punchRuleId, date) ; 
		return processTimeRuleDTO(dayPunchTimeRule);
	}
	
	private PunchTimeRuleDTO processTimeRuleDTO(PunchTimeRule dayPunchTimeRule) {
		PunchTimeRuleDTO dto = null;
		if(null != dayPunchTimeRule){
			dto = ConvertHelper.convert(dayPunchTimeRule, PunchTimeRuleDTO.class); 
			dto.setAfternoonArriveTime(null!=dayPunchTimeRule.getAfternoonArriveTimeLong()?dayPunchTimeRule.getAfternoonArriveTimeLong():punchService.convertTimeToGMTMillisecond(dayPunchTimeRule.getAfternoonArriveTime()));
			dto.setNoonLeaveTime(null!=dayPunchTimeRule.getNoonLeaveTimeLong()?dayPunchTimeRule.getNoonLeaveTimeLong():punchService.convertTimeToGMTMillisecond(dayPunchTimeRule.getNoonLeaveTime()));
			dto.setStartEarlyTime(null!=dayPunchTimeRule.getStartEarlyTimeLong()?dayPunchTimeRule.getStartEarlyTimeLong():punchService.convertTimeToGMTMillisecond(dayPunchTimeRule.getStartEarlyTime()));
			dto.setStartLateTime(null!=dayPunchTimeRule.getStartLateTimeLong()?dayPunchTimeRule.getStartLateTimeLong():punchService.convertTimeToGMTMillisecond(dayPunchTimeRule.getStartLateTime()));
			
			dto.setEndEarlyTime(dto.getStartEarlyTime() + (null!=dayPunchTimeRule.getWorkTimeLong()?dayPunchTimeRule.getWorkTimeLong():punchService.convertTimeToGMTMillisecond(dayPunchTimeRule.getWorkTime())));
			dto.setDaySplitTime(null!=dayPunchTimeRule.getDaySplitTimeLong()?dayPunchTimeRule.getDaySplitTimeLong():punchService.convertTimeToGMTMillisecond(dayPunchTimeRule.getDaySplitTime()));
		}
		return dto;
	}
	/**
	 * 计算请假结束日的时间
	 * */
	private void calculateAbsentEndDate(MyDate myDate,PunchTimeRuleDTO dto,Long endTimeLong ){
		long actualLong = 0l;
		if(endTimeLong<=dto.getStartLateTime()){
			//0.x<最晚上班时间
			//不管了
		}else if(endTimeLong<=dto.getNoonLeaveTime()){
			//1.最晚上班时间<x<=午休开始时间
			// 请假时间-最晚上班
			actualLong = endTimeLong -dto.getStartLateTime();
		}else if(endTimeLong<=dto.getAfternoonArriveTime()){
			//2.午休开始时间<x<=午休结束时间  
			// 中午下班-最晚上班
			actualLong = dto.getNoonLeaveTime()-dto.getStartLateTime();
		}else if(endTimeLong<=dto.getEndEarlyTime()){
			//3.午休结束时间<x<=最早下班时间 
			//请假时间-下午上班+中午下班-最晚上班
			actualLong = endTimeLong -dto.getAfternoonArriveTime() + dto.getNoonLeaveTime()-dto.getStartLateTime();
		}else{
			//4.最早下班时间<x
			//请假日期+1
			int days = myDate.getDays()+1;
			myDate.setDays(days);
		}
		caculateAbsentDate(myDate,actualLong);
	}
	/**
	 * 计算请假开始日的时间
	 * */
	private void calculateAbsentBeginDate(MyDate myDate,PunchTimeRuleDTO dto,Long fromTimeLong ){
		long actualLong = 0l;
		if(fromTimeLong<=dto.getStartLateTime()){
			//0.x<最晚上班时间
			//请假日期+1
			int days = myDate.getDays()+1;
			myDate.setDays(days);
		}else if(fromTimeLong<=dto.getNoonLeaveTime()){
			//1.最晚上班时间<x<=午休开始时间
			// 中午下班-请假时间 +最早下班-下午上班
			actualLong = dto.getNoonLeaveTime()-fromTimeLong + dto.getEndEarlyTime()-dto.getAfternoonArriveTime();
		}else if(fromTimeLong<=dto.getAfternoonArriveTime()){
			//2.午休开始时间<x<=午休结束时间  
			//最早下班-下午上班
			actualLong =   dto.getEndEarlyTime()-dto.getAfternoonArriveTime();
		}else if(fromTimeLong<=dto.getEndEarlyTime()){
			//3.午休结束时间<x<=最早下班时间
			//最早下班-请假时间
			actualLong =   dto.getEndEarlyTime()-fromTimeLong;
		}else{
			//4.最早下班时间<x
			//不管了
		}
		caculateAbsentDate(myDate, actualLong);
	}
	/**
	 * 把时间Long转换成日,时,分,秒
	 * */
	private void caculateAbsentDate(MyDate myDate,  long actualLong) {
		actualLong = actualLong / 1000 / 60;
		int minutes = myDate.getMinutes() + Long.valueOf(actualLong % 60).intValue();
		int hous = myDate.getHours() + Long.valueOf(actualLong / 60).intValue();
		if(minutes > 60){
			minutes -=60;
			hous+=1;
		}
		if(hous >24){
			hous -=60;
			int days = myDate.getDays()+1;
			myDate.setDays(days);
		}
		myDate.setHours(hous);
		myDate.setMinutes(minutes);
		
	}


	private String calculateOneDayTime(Long fromTimeLong, Long endTimeLong, PunchTimeRule punchTimeRule){
		Time fromTime = Time.valueOf(timeSF.get().format(new Date(fromTimeLong)));
		Time endTime = Time.valueOf(timeSF.get().format(new Date(endTimeLong)));
		//1. 如果请假开始时间小于最晚上班时间，以最晚上班时间为准
		//林园说就以实际请假的开始时间算
//		if (fromTime.getTime() < punchTimeRule.getStartLateTime().getTime()) {
//			fromTime = punchTimeRule.getStartLateTime();
//		}
		long deltaMs = endTime.getTime() - fromTime.getTime();
		//2. 判断是否包含午休时间，只要开始时间小于等于午休开始时间且结束时间大于等于午休结束时间即可
		if (!fromTime.after(punchTimeRule.getNoonLeaveTime()) && !endTime.before(punchTimeRule.getAfternoonArriveTime())) {
			deltaMs = deltaMs - (punchTimeRule.getAfternoonArriveTime().getTime()-punchTimeRule.getNoonLeaveTime().getTime());
		}
		return convertMsToActualResult(deltaMs, punchTimeRule.getWorkTime());
	}
	
	//用于计算每天的实际请假时长（跨天）
	private void calculateEverydayResultTime(Long userId, PunchRule punchRule, PunchTimeRule fromPunchTimeRule, PunchTimeRule endPunchTimeRule,
			Date fromTime, Date endTime, List<ApprovalDayActualTime> approvalDayActualTimeList) {
		Date date = fromTime;
		Time earlyLeaveTime = punchService.getEndTime(fromPunchTimeRule.getStartEarlyTime(), fromPunchTimeRule.getWorkTime());
		//计算开始时间所在的日期
		if (!Time.valueOf(timeSF.get().format(fromTime)).before(earlyLeaveTime)) {
			//如果开始时间在最早下班时间之后，则不需要计算
		}else if (!Time.valueOf(timeSF.get().format(fromTime)).after(fromPunchTimeRule.getStartLateTime())) {
			//如果开始时间在最晚上班之前，表示是一完整天
			ApprovalDayActualTime actualTime = new ApprovalDayActualTime();
			actualTime.setUserId(userId);
			actualTime.setTimeDate(new java.sql.Date(fromTime.getTime()));
			actualTime.setActualResult(new MyDate(1, 0, 0).toString());
			approvalDayActualTimeList.add(actualTime);
		}else {
			//如果开始时间在最晚上班时间到最早下班时间之间，则计算小时分钟
			ApprovalDayActualTime actualTime = new ApprovalDayActualTime();
			actualTime.setUserId(userId);
			actualTime.setTimeDate(new java.sql.Date(fromTime.getTime()));
			try {
				actualTime.setActualResult(calculateOneDayTime(fromTime.getTime(), datetimeSF.get().parse(dateSF.get().format(fromTime)+" "+timeSF.get().format(earlyLeaveTime)).getTime(), fromPunchTimeRule));
			} catch (ParseException e) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"calculate time error");
			}
			approvalDayActualTimeList.add(actualTime);
		}
		
		while(true) {
			date = new Date(date.getTime() + DAY_MILLISECONDGMT);
			
			if (dateSF.get().format(date).equals(dateSF.get().format(endTime))) {
				break;
			}
			//如果是工作日，则表示是完整天
			if ( isWorkDay(date, punchRule)) {
				ApprovalDayActualTime actualTime = new ApprovalDayActualTime();
				actualTime.setUserId(userId);
				actualTime.setTimeDate(new java.sql.Date(date.getTime()));
				actualTime.setActualResult(new MyDate(1, 0, 0).toString());
				approvalDayActualTimeList.add(actualTime);
			}
		}
		
		//计算结束时间所在日期
		if (!Time.valueOf(timeSF.get().format(endTime)).after(endPunchTimeRule.getStartLateTime())) {
			//如果结束时间在最晚上班时间之前，则不计算
		}else if (!Time.valueOf(timeSF.get().format(endTime)).before(earlyLeaveTime)) {
			//如果结束时间在最早下班时间之后，则表示一完整天
			ApprovalDayActualTime actualTime = new ApprovalDayActualTime();
			actualTime.setUserId(userId);
			actualTime.setTimeDate(new java.sql.Date(endTime.getTime()));
			actualTime.setActualResult(new MyDate(1, 0, 0).toString());
			approvalDayActualTimeList.add(actualTime);
		}else {
			//如果结束时间在最晚上班时间到最早下班时间之间，则计算小时分钟
			ApprovalDayActualTime actualTime = new ApprovalDayActualTime();
			actualTime.setUserId(userId);
			actualTime.setTimeDate(new java.sql.Date(endTime.getTime()));
			try {
				actualTime.setActualResult(calculateOneDayTime(datetimeSF.get().parse(dateSF.get().format(endTime)+" "+timeSF.get().format(endPunchTimeRule.getStartLateTime())).getTime(), endTime.getTime(), endPunchTimeRule));
			} catch (ParseException e) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"calculate time error");
			}
			approvalDayActualTimeList.add(actualTime);
		}
	}

	//用于计算完整的天到每天实际时长表中（不跨天）
	private void calculateEverydayResultWholeDay(Long userId, PunchRule punchRule, Date fromTime, Date endTime, boolean containFromTime, boolean containEndTime, List<ApprovalDayActualTime> approvalDayActualTimeList) {
		Date date = fromTime;
		while(true){
			if (date.getTime() == fromTime.getTime()) {
				if (containFromTime) {
					ApprovalDayActualTime actualTime = new ApprovalDayActualTime();
					actualTime.setUserId(userId);
					actualTime.setTimeDate(new java.sql.Date(fromTime.getTime()));
					actualTime.setActualResult(new MyDate(1, 0, 0).toString());
					approvalDayActualTimeList.add(actualTime);
				}
			}else if (dateSF.get().format(date).equals(dateSF.get().format(endTime))) {
				if (containEndTime) {
					ApprovalDayActualTime actualTime = new ApprovalDayActualTime();
					actualTime.setUserId(userId);
					actualTime.setTimeDate(new java.sql.Date(endTime.getTime()));
					actualTime.setActualResult(new MyDate(1, 0, 0).toString());
					approvalDayActualTimeList.add(actualTime);
				}
				break;
			}else {
				if (isWorkDay(date, punchRule)) {
					ApprovalDayActualTime actualTime = new ApprovalDayActualTime();
					actualTime.setUserId(userId);
					actualTime.setTimeDate(new java.sql.Date(date.getTime()));
					actualTime.setActualResult(new MyDate(1, 0, 0).toString());
					approvalDayActualTimeList.add(actualTime);
				}
			}
			date = new Date(date.getTime() + DAY_MILLISECONDGMT);
		}
		
	}

	private String convertDayToActualResult(long deltaDay) {
		MyDate myDate = new MyDate();
		myDate.setDays(Long.valueOf(deltaDay).intValue());
		myDate.setHours(0);
		myDate.setMinutes(0);
		return myDate.toString();
	}

//	private String convertMsToActualResult(long finalAbsentTime, long deltaWorkTime) {
//		//如果两者相等，不计算天数
//		//两者相除结果为天数，余数再计算小数分钟数
//		MyDate myDate = new MyDate();
//		if (finalAbsentTime > deltaWorkTime) {
//			myDate.setDays(Long.valueOf(finalAbsentTime / deltaWorkTime).intValue());
//			finalAbsentTime = finalAbsentTime % deltaWorkTime;
//		}
//		finalAbsentTime = finalAbsentTime / 1000 / 60;
//		myDate.setMinutes(Long.valueOf(finalAbsentTime % 60).intValue());
//		myDate.setHours(Long.valueOf(finalAbsentTime / 60).intValue());
//		
//		return myDate.toString();
//	}

	private long calculateNotWorkDay(Date fromTime, Date endTime, Long userId, PunchRule punchRule) {
		long deltaDay = calculateDeltaDay(fromTime, endTime);
		if (deltaDay <= 1) {
			return 0L;
		}
		long result = 0;
		for(long i = 1; i < deltaDay; i++){
			if (!isWorkDay(new Date(fromTime.getTime() + i*DAY_MILLISECONDGMT), punchRule)) {
				result++;
			}
		}
		return result;
	}

	private long calculateDeltaDay(Date fromTime, Date endTime) {
		try {
			return (dateSF.get().parse(dateSF.get().format(endTime)).getTime() - dateSF.get().parse(dateSF.get().format(fromTime)).getTime()) / (DAY_MILLISECONDGMT);
		} catch (ParseException e) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"calculate delta day error");
		}
	}

	//不管对于开始时间还是结束时间，都需要格式化，如果小于最晚上班时间则格式化最晚上班时间，如果大于最早下班时间则格式化为最早下班时间
	private Date formatTime(Long time, Time startLateTime, Time endEarlyTime) {
		if (Time.valueOf(timeSF.get().format(new Date(time))).getTime() < startLateTime.getTime()) {
			try {
				return datetimeSF.get().parse(dateSF.get().format(new Date(time)) + " " + timeSF.get().format(startLateTime));
			} catch (ParseException e) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"format time error");
			}
		}
		
		if (Time.valueOf(timeSF.get().format(new Date(time))).getTime() > endEarlyTime.getTime()) {
			try {
				return datetimeSF.get().parse(dateSF.get().format(new Date(time)) + " " + timeSF.get().format(endEarlyTime));
			} catch (ParseException e) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"format end time error");
			}
		}
		
		return new Date(time);
	}
	
//	private Date formatFromTime(Long fromTime, Time startLateTime) {
//		if (Time.valueOf(timeSF.get().format(new Date(fromTime))).getTime() < startLateTime.getTime()) {
//			try {
//				return datetimeSF.get().parse(dateSF.get().format(new Date(fromTime)) + " " + timeSF.get().format(startLateTime));
//			} catch (ParseException e) {
//				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//						"format from time error");
//			}
//		}
//		return new Date(fromTime);
//	}
//
//	private Date formatEndTime(Long endTime, Time endEarlyTime) {
//		if (Time.valueOf(timeSF.get().format(new Date(endTime))).getTime() > endEarlyTime.getTime()) {
//			try {
//				return datetimeSF.get().parse(dateSF.get().format(new Date(endTime)) + " " + timeSF.get().format(endEarlyTime));
//			} catch (ParseException e) {
//				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//						"format end time error");
//			}
//		}
//		return new Date(endTime);
//	}

	private String convertMsToActualResult(long deltaMs, Time workTime) {
		MyDate thisDate = new MyDate();
		if (deltaMs >= workTime.getTime()+MILLISECONDGMT) {
			thisDate.setDays(1);
			thisDate.setHours(0);
			thisDate.setMinutes(0);
			return thisDate.toString();
		}
		deltaMs = deltaMs / 1000 / 60;
		thisDate.setMinutes(Long.valueOf(deltaMs % 60).intValue());
		thisDate.setHours(Long.valueOf(deltaMs / 60).intValue());
		
		return thisDate.toString();
	}

	private class MyDate {
		private int days=0;
		private int hours=0;
		private int minutes=0;
		
		public MyDate() {
			super();
		}
		public MyDate(int days, int hours, int minutes) {
			super();
			this.days = days;
			this.hours = hours;
			this.minutes = minutes;
		}
		public MyDate(String actual) {
			super();
			String[] strings = actual.split("\\.");
			if(strings.length ==3){
				days = Integer.valueOf(strings[0]);
				hours = Integer.valueOf(strings[1]);
				minutes = Integer.valueOf(strings[2]);
			}
		}
		public int getDays() {
			return days;
		}
		public void setDays(int days) {
			this.days = days;
		}
		public int getHours() {
			return hours;
		}
		public void setHours(int hours) {
			this.hours = hours;
		}
		public int getMinutes() {
			return minutes;
		}
		public void setMinutes(int minutes) {
			this.minutes = minutes;
		}
		@Override
		public String toString() {
			return days + "." + hours + "." + minutes;
		}
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
	public String postProcessCreateApprovalRequest(Long userId, ApprovalOwnerInfo ownerInfo, ApprovalRequest approvalRequest,
			CreateApprovalRequestBySceneCommand cmd) {
		//添加请假时间
		createTimeRange(userId, approvalRequest.getId(), cmd.getTimeRangeList());
		createApprovalDayActualTime(userId, approvalRequest.getId(), approvalRequest.getApprovalDayActualTimeList());
		//添加工作流
		//'请假类型：${absentCategory}\n请假时间${beginTime}至${endTime}'
    	Map<String, String> map = new HashMap<String, String>();  
    	ApprovalCategory category = approvalCategoryProvider.findApprovalCategoryById(approvalRequest.getCategoryId());
        map.put("absentCategory", category==null?"":category.getCategoryName());

		//请假时间不能为空
		if (ListUtils.isEmpty(cmd.getTimeRangeList()) || checkTimeEmpty(cmd.getTimeRangeList())) {
			throw RuntimeErrorException.errorWith(ApprovalServiceErrorCode.SCOPE, ApprovalServiceErrorCode.ABSENCE_EMPTY_TIME,
					"time cannot be empty");
		}
		 
        TimeRange range = cmd.getTimeRangeList().get(0);
        SimpleDateFormat sdFormat = new SimpleDateFormat("yy-MM-dd HH:mm");
        map.put("beginTime", sdFormat.format(new Date(range.getFromTime())) ); 
        map.put("endTime", sdFormat.format(new Date(range.getEndTime())) ); 
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
		e.setKey(this.localeStringService.getLocalizedString(PunchConstants.PUNCH_FLOW_SCOPE,
				"category", RentalNotificationTemplateCode.locale, ""));
		ApprovalCategory category = this.approvalCategoryProvider.findApprovalCategoryById(approvalRequest.getCategoryId());
		if (null == category) {
			LOGGER.debug("category can not be found : " + approvalRequest.getCategoryId());
		} else {
			e.setValue(category.getCategoryName());
		}
		entities.add(e);

		List<ApprovalTimeRange> approvalTimeRangeList = approvalTimeRangeProvider.listApprovalTimeRangeByOwnerId(approvalRequest.getId());
		Map<String, String> map = new HashMap<String, String>();  
		SimpleDateFormat sdFormat = new SimpleDateFormat("yy-MM-dd HH:mm");
        map.put("beginTime", sdFormat.format( approvalTimeRangeList.get(0).getFromTime()) ); 
        map.put("endTime", sdFormat.format( approvalTimeRangeList.get(0).getEndTime()) ); 
		String contentString = localeTemplateService.getLocaleTemplateString(PunchConstants.PUNCH_FLOW_SCOPE ,
				PunchConstants.PUNCH_FLOW_REQUEST_TIME , "zh_CN", map, "");
		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
		e.setKey(this.localeStringService.getLocalizedString(PunchConstants.PUNCH_FLOW_SCOPE ,
				"absentTime", PunchConstants.locale, "")); 
		e.setValue(contentString);
		entities.add(e);
		
		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
		e.setKey(this.localeStringService.getLocalizedString(PunchConstants.PUNCH_FLOW_SCOPE ,
				"absentLength", PunchConstants.locale, "")); 
		String[] times = approvalTimeRangeList.get(0).getActualResult().split("\\.");
		StringBuffer sb = new StringBuffer();
		sb.append( times[0].equals("0")?"":times[0]+ localeStringService.getLocalizedString(ApprovalTypeTemplateCode.TIME_SCOPE,
				ApprovalTypeTemplateCode.DAY,UserContext.current().getUser().getLocale(),""));
		sb.append( times[1].equals("0")?"":times[1]+ localeStringService.getLocalizedString(ApprovalTypeTemplateCode.TIME_SCOPE,
				ApprovalTypeTemplateCode.HOUR,UserContext.current().getUser().getLocale(),""));
		sb.append( times[2].equals("0")?"":times[2]+ localeStringService.getLocalizedString(ApprovalTypeTemplateCode.TIME_SCOPE,
				ApprovalTypeTemplateCode.MIN,UserContext.current().getUser().getLocale(),""));
		e.setValue(sb.toString());
		entities.add(e);
		
		
		entities.addAll(getPostFlowEntities(approvalRequest));
		return entities;
		
	}


	private void createApprovalDayActualTime(Long userId, Long requestId,
			List<ApprovalDayActualTime> approvalDayActualTimeList) {
		approvalDayActualTimeList.forEach(a->{
			a.setOwnerId(requestId);
			a.setCreatorUid(userId);
			a.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		});
		approvalDayActualTimeProvider.createApprovalDayActualTimes(approvalDayActualTimeList);
	}


	//实际请假时长放在前置处理器中计算
	private void createTimeRange(Long userId, Long requestId, List<TimeRange> timeRangeList) {
		approvalTimeRangeProvider.createApprovalTimeRanges(convertTimeRangeList(timeRangeList, userId, requestId));
	}

	private List<ApprovalTimeRange> convertTimeRangeList(List<TimeRange> timeRangeList, Long userId, Long requestId) {
		return timeRangeList.stream().map(t->{
			ApprovalTimeRange approvalTimeRange = new ApprovalTimeRange();
			approvalTimeRange.setOwnerId(requestId);
			approvalTimeRange.setFromTime(new Timestamp(t.getFromTime()));
			approvalTimeRange.setEndTime(new Timestamp(t.getEndTime()));
			approvalTimeRange.setType(t.getType());
			approvalTimeRange.setActualResult(t.getActualResult());
			approvalTimeRange.setCreatorUid(userId);
			approvalTimeRange.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			return approvalTimeRange;
		}).collect(Collectors.toList());
	}


	@Override
	public List<RequestDTO> processListApprovalRequest(List<ApprovalRequest> approvalRequestList) {
		List<RequestDTO> resultList = approvalRequestList.stream().map(a->{
			RequestDTO absenceRequest = new RequestDTO();
			absenceRequest.setRequestId(a.getId());
			absenceRequest.setReason(a.getReason());
			absenceRequest.setNickName(approvalService.getUserName(a.getCreatorUid(), a.getOwnerId()));
			absenceRequest.setCategoryName(approvalCategoryProvider.findApprovalCategoryById(a.getCategoryId()).getCategoryName());
			List<ApprovalTimeRange> approvalTimeRangeList = approvalTimeRangeProvider.listApprovalTimeRangeByOwnerId(a.getId());
			List<TimeRange> timeRangeList = new ArrayList<>();
			String timeTotal = "";
			for (ApprovalTimeRange approvalTimeRange : approvalTimeRangeList) {
				TimeRange timeRange = new TimeRange();
				timeRange.setType(approvalTimeRange.getType());
				timeRange.setFromTime(approvalTimeRange.getFromTime().getTime());
				timeRange.setEndTime(approvalTimeRange.getEndTime().getTime());
				timeRange.setActualResult(approvalTimeRange.getActualResult());
				timeRangeList.add(timeRange);
				timeTotal = calculateTimeTotal(timeTotal, timeRange.getActualResult());
			}
			absenceRequest.setTimeRangeList(timeRangeList);
			absenceRequest.setTimeTotal(timeTotal);
			return absenceRequest;
		}).collect(Collectors.toList());
		
		return resultList;
	}


	@Override
	public String processMessageToCreatorBody(ApprovalRequest approvalRequest, String reason) {
		String scope = null;
		int code = 0;
		Map<String, Object> map = new HashMap<>();
		map.put("time", getAbsenceTime(approvalRequest));
		if (approvalRequest.getApprovalStatus().byteValue() == ApprovalStatus.AGREEMENT.getCode()) {
			scope = ApprovalNotificationTemplateCode.SCOPE;
			code = ApprovalNotificationTemplateCode.ABSENCE_APPROVED;
		}else {
			scope = ApprovalNotificationTemplateCode.SCOPE;
			code = ApprovalNotificationTemplateCode.ABSENCE_REJECTED;
			map.put("reason", StringUtils.isBlank(reason)?approvalRequest.getReason():reason);
			map.put("approver", approvalService.getUserName(approvalRequest.getOperatorUid(), approvalRequest.getOwnerId()));
		}
		return localeTemplateService.getLocaleTemplateString(scope, code, UserContext.current().getUser().getLocale(), map, "");
	}
	
	private String getAbsenceTime(ApprovalRequest approvalRequest){
		List<ApprovalTimeRange> approvalTimeRangeList = approvalTimeRangeProvider.listApprovalTimeRangeByOwnerId(approvalRequest.getId());
		StringBuilder sb = new StringBuilder();
		if (ListUtils.isNotEmpty(approvalTimeRangeList)) {
			approvalTimeRangeList.forEach(a->sb.append(dateMinutesSF.get().format(a.getFromTime())).append(" ~ ").append(dateMinutesSF.get().format(a.getEndTime())).append(", "));
			if (sb.length()>1) {
				return sb.substring(0, sb.length()-2);
			}
		}
		return "";
	}

	@Override
	public String processMessageToNextLevelBody(ApprovalRequest approvalRequest) {
		String scope = null;
		int code = 0;
		Map<String, Object> map = new HashMap<>();
		map.put("creatorName", approvalService.getUserName(approvalRequest.getCreatorUid(), approvalRequest.getOwnerId()));
		map.put("time", getAbsenceTime(approvalRequest));
		//当前级别为0表示用户刚提交
		if (approvalRequest.getCurrentLevel().byteValue() == (byte)0) {
			scope = ApprovalNotificationTemplateCode.SCOPE;
			code = ApprovalNotificationTemplateCode.ABSENCE_COMMIT_REQUEST;
		}else {
			scope = ApprovalNotificationTemplateCode.SCOPE;
			code = ApprovalNotificationTemplateCode.ABSENCE_TO_NEXT_LEVEL;
			map.put("approver", approvalService.getUserName(approvalRequest.getOperatorUid(), approvalRequest.getOwnerId()));
		}
		return localeTemplateService.getLocaleTemplateString(scope, code, UserContext.current().getUser().getLocale(), map, "");
	}
	private String processBriefRequestTitle(ApprovalRequest a , String timeTotal ,Long fromTime ,Long endTime) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		 
		// 初次提交
		String scope = ApprovalLogTitleTemplateCode.SCOPE;
		int code = ApprovalLogTitleTemplateCode.ABSENCE_TITLE;
		map.put("nickName",approvalService.getUserName(a.getCreatorUid(), a.getOwnerId()) );
		map.put("category",
				approvalService.findApprovalCategoryById(a.getCategoryId()).getCategoryName());
		String[] times = timeTotal.split("\\.");
		map.put("day", times[0].equals("0")?"":times[0]+ localeStringService.getLocalizedString(ApprovalTypeTemplateCode.TIME_SCOPE,
				ApprovalTypeTemplateCode.DAY,UserContext.current().getUser().getLocale(),"") );
		map.put("hour", times[1].equals("0")?"":times[1]+ localeStringService.getLocalizedString(ApprovalTypeTemplateCode.TIME_SCOPE,
				ApprovalTypeTemplateCode.HOUR,UserContext.current().getUser().getLocale(),""));
		map.put("min", times[2].equals("0")?"":times[2]+ localeStringService.getLocalizedString(ApprovalTypeTemplateCode.TIME_SCOPE,
				ApprovalTypeTemplateCode.MIN,UserContext.current().getUser().getLocale(),""));
		SimpleDateFormat mmDDSF = new SimpleDateFormat("MM-dd HH:mm");
		map.put("beginDate", mmDDSF.format(new Date(fromTime)));
		map.put("endDate", mmDDSF.format(new Date(endTime)));
		String result = localeTemplateService.getLocaleTemplateString(scope, code, UserContext.current().getUser().getLocale(), map, "");
		
		return result;
	}

 
	@Override
	public String ApprovalLogAndFlowOfRequestResponseTitle(
			ApprovalRequest a) { 
		BriefApprovalRequestDTO briefApprovalRequestDTO = super.processBriefApprovalRequest(a);
		String timeTotal = null;

		Long fromTime = null;
		Long endTime =null ;
		if (a.getTimeFlag().byteValue() == TrueOrFalseFlag.TRUE.getCode()) {
			List<TimeRange> timeRangeList = briefApprovalRequestDTO.getTimeRangeList();
			for (TimeRange timeRange : timeRangeList) {
				timeTotal = calculateTimeTotal(timeTotal, timeRange.getActualResult());
				if(null == fromTime || fromTime > timeRange.getFromTime())
					fromTime = timeRange.getFromTime();
				if(null == endTime || endTime < timeRange.getEndTime())
					endTime = timeRange.getEndTime();
			}
		}
		Map<String, Object> map = new HashMap<>();
		 
		 
		String scope = ApprovalLogTitleTemplateCode.SCOPE;
		int code = ApprovalLogTitleTemplateCode.ABSENCE_MAIN_TITLE;
		map.put("category",
				approvalService.findApprovalCategoryById(a.getCategoryId()).getCategoryName());
		String[] times = timeTotal.split("\\.");
		map.put("day", times[0].equals("0")?"":times[0]+ localeStringService.getLocalizedString(ApprovalTypeTemplateCode.TIME_SCOPE,
				ApprovalTypeTemplateCode.DAY,UserContext.current().getUser().getLocale(),""));
		map.put("hour", times[1].equals("0")?"":times[1]+ localeStringService.getLocalizedString(ApprovalTypeTemplateCode.TIME_SCOPE,
				ApprovalTypeTemplateCode.HOUR,UserContext.current().getUser().getLocale(),""));
		map.put("min", times[2].equals("0")?"":times[2]+ localeStringService.getLocalizedString(ApprovalTypeTemplateCode.TIME_SCOPE,
				ApprovalTypeTemplateCode.MIN,UserContext.current().getUser().getLocale(),""));
		SimpleDateFormat mmDDSF = new SimpleDateFormat("MM-dd HH:mm");
		map.put("beginDate", mmDDSF.format(new Date(fromTime)));
		map.put("endDate", mmDDSF.format(new Date(endTime)));
		
		String result = localeTemplateService.getLocaleTemplateString(scope, code, UserContext.current().getUser().getLocale(), map, "");
		
		return result;
	}
	

	
	@Override
	public BriefApprovalRequestDTO processApprovalRequestByScene(ApprovalRequest approvalRequest) {
		BriefApprovalRequestDTO briefApprovalRequestDTO = super.processBriefApprovalRequest(approvalRequest);
		String timeTotal = null;

		Long fromTime = null;
		Long endTime =null ;
		if (approvalRequest.getTimeFlag().byteValue() == TrueOrFalseFlag.TRUE.getCode()) {
			List<TimeRange> timeRangeList = briefApprovalRequestDTO.getTimeRangeList();
			for (TimeRange timeRange : timeRangeList) {
				timeTotal = calculateTimeTotal(timeTotal, timeRange.getActualResult());
				if(null == fromTime || fromTime > timeRange.getFromTime())
					fromTime = timeRange.getFromTime();
				if(null == endTime || endTime < timeRange.getEndTime())
					endTime = timeRange.getEndTime();
			}
		}
		briefApprovalRequestDTO.setDescription(timeTotal);
		briefApprovalRequestDTO.setTitle(ApprovalLogAndFlowOfRequestResponseTitle(approvalRequest));
		return briefApprovalRequestDTO;
	}
	

	@Override
	public void calculateRangeStat(ApprovalRequest approvalRequest) { 
		// 取到所有的ranges-目前一个request对应一个
		List<ApprovalTimeRange>  timeRanges = approvalTimeRangeProvider.listApprovalTimeRangeByOwnerId(approvalRequest.getId());
		for(ApprovalTimeRange timeRange : timeRanges){
			if(monthSF.get().format(timeRange.getFromTime()).equals(monthSF.get().format(timeRange.getEndTime()))){
				// 1.如果不跨月:直接累加
				MyDate myDate = new MyDate(timeRange.getActualResult());
				createOrUpdateRangeStat(approvalRequest.getOwnerId(),approvalRequest.getOwnerType(),approvalRequest.getCreatorUid(),
						monthSF.get().format(timeRange.getFromTime()),approvalRequest.getApprovalType(),approvalRequest.getCategoryId(),myDate);	
				
			}else{
				// 2.如果跨月:分月累加
				PunchRule punchRule = punchService.getPunchRule(approvalRequest.getOwnerType(), approvalRequest.getOwnerId(), approvalRequest.getCreatorUid());
				Calendar startCalendar = Calendar.getInstance();
				startCalendar.setTime(timeRange.getFromTime());
				Calendar endCalendar = Calendar.getInstance();
				endCalendar.setTime(timeRange.getEndTime()); 
				for(;monthSF.get().format(startCalendar.getTime()).equals(monthSF.get().format(endCalendar.getTime()));){
					Date fromTime = startCalendar.getTime();
					startCalendar.add(Calendar.MONTH, 1);
					startCalendar.set(Calendar.DAY_OF_MONTH, startCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
					PunchTimeRule ptr = punchService.getPunchTimeRuleByRuleIdAndDate(punchRule.getId(), startCalendar.getTime()) ; 
					Long startEarlyTimeLong = null!=ptr.getStartEarlyTimeLong()?ptr.getStartEarlyTimeLong():punchService.convertTimeToGMTMillisecond(ptr.getStartEarlyTime());
					//把结束时间定位为下一个月第一天的上班时间前一小时
					startCalendar.set(Calendar.HOUR,(int) (startEarlyTimeLong/3600000)-1);
					MyDate myDate = processMyDate(fromTime, startCalendar.getTime(), punchRule);
					createOrUpdateRangeStat(approvalRequest.getOwnerId(),approvalRequest.getOwnerType(),approvalRequest.getCreatorUid(),
							monthSF.get().format(fromTime),approvalRequest.getApprovalType(),approvalRequest.getCategoryId(),myDate);	
				}
				MyDate myDate = processMyDate(startCalendar.getTime(),endCalendar.getTime(), punchRule);
				createOrUpdateRangeStat(approvalRequest.getOwnerId(),approvalRequest.getOwnerType(),approvalRequest.getCreatorUid(),
						monthSF.get().format(startCalendar.getTime()),approvalRequest.getApprovalType(),approvalRequest.getCategoryId(),myDate);	
			}		
		}
		
	}


	private void createOrUpdateRangeStat(Long ownerId, String ownerType, Long creatorUid,
			String punchMonth,Byte approvalType,Long categoryId , MyDate myDate) {
		 
		ApprovalRangeStatistic  statistic = approvalRangeStatisticProvider.getApprovalRangeStatisticByParams(punchMonth, ownerType, ownerId, creatorUid, approvalType, categoryId);
		if(statistic == null ){
			statistic = new ApprovalRangeStatistic();
			statistic.setOwnerType(ownerType);
			statistic.setOwnerId(ownerId);
			statistic.setUserId(creatorUid);
			statistic.setPunchMonth(punchMonth);
			statistic.setApprovalType(approvalType);
			statistic.setCategoryId(categoryId);
			statistic.setActualResult(myDate.toString());
			statistic.setCreatorUid(UserContext.current().getUser().getId());
			statistic.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			approvalRangeStatisticProvider.createApprovalRangeStatistic(statistic);
		}else{
			MyDate oldMyDate = new MyDate(statistic.getActualResult());
			int minutes = oldMyDate.getMinutes()+ myDate.getMinutes();
			int hours = oldMyDate.getHours() + myDate.getHours();
			int days = oldMyDate.getDays() + myDate.getDays();
			if(minutes > 60){
				minutes -= 60;
				hours += 1;
			}
			if(hours >24){
				hours -= 60;
				days += 1;
				 
			}
			myDate.setMinutes(minutes);
			myDate.setHours(hours);
			myDate.setDays(days);
			statistic.setActualResult(myDate.toString());
			approvalRangeStatisticProvider.updateApprovalRangeStatistic(statistic);
		}
		
		
	}
 
	
}
