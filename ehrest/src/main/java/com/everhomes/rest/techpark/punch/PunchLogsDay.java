package com.everhomes.rest.techpark.punch;
 
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

 /**
 * <ul>  
 * <li>PunchDay：打卡日期DD(1到31) </li>
 * <li>punchStatus：打卡状态  如 迟到 早退 参考{@link com.everhomes.rest.techpark.punch.PunchStatus}</li>
 * <li>morningPunchStatus：早上打卡状态  如 迟到 早退 参考{@link com.everhomes.rest.techpark.punch.PunchStatus}</li>
 * <li>afternoonPunchStatus：下午打卡状态  如 迟到 早退 参考{@link com.everhomes.rest.techpark.punch.PunchStatus}</li>
 * <li>approvalStatus：审批状态  如 迟到 早退 参考{@link com.everhomes.rest.techpark.punch.ApprovalStatus}</li>
 * <li>morningApprovalStatus：早上审批状态  如 迟到 早退 参考{@link com.everhomes.rest.techpark.punch.ApprovalStatus}</li>
 * <li>afternoonApprovalStatus：下午审批状态  如 迟到 早退 参考{@link com.everhomes.rest.techpark.punch.ApprovalStatus}</li>
 * <li>punchStatusNew：打卡状态  如 迟到 早退 参考{@link com.everhomes.rest.techpark.punch.PunchStatus}</li>
 * <li>morningPunchStatusNew：早上打卡状态  如 迟到 早退 参考{@link com.everhomes.rest.techpark.punch.PunchStatus}</li>
 * <li>afternoonPunchStatusNew：下午打卡状态  如 迟到 早退 参考{@link com.everhomes.rest.techpark.punch.PunchStatus}</li>
 * <li>approvalStatusNew：审批状态  如 迟到 早退 参考{@link com.everhomes.rest.techpark.punch.ApprovalStatus}</li>
 * <li>morningApprovalStatusNew：早上审批状态  如 迟到 早退 参考{@link com.everhomes.rest.techpark.punch.ApprovalStatus}</li>
 * <li>afternoonApprovalStatusNew：下午审批状态  如 迟到 早退 参考{@link com.everhomes.rest.techpark.punch.ApprovalStatus}</li>
 * <li>exceptionStatus：异常状态:1-异常0-正常 参考{@link com.everhomes.rest.techpark.punch.ExceptionStatus}</li>
 * <li>PunchLogs: 打卡记录list {@link com.everhomes.rest.techpark.punch.PunchLogDTO}</li>
 * <li>workTime: 工作时长 Long</li>
 * <li>requestFlag: 全天异常申请flag:1-有申请 0-无申请 空-无申请</li>
 * <li>morningRequestFlag: 上午异常申请flag:1-有申请 0-无申请 空-无申请</li>
 * <li>afternoonRequestFlag: 下午异常申请flag:1-有申请 0-无申请 空-无申请</li>
 * <li>requestToken: 全天异常申请的申请标识</li>
 * <li>morningRequestToken: 上午异常申请的申请标识</li>
 * <li>afternoonRequestToken: 下午异常申请的申请标识</li>
 * <li>arriveTime:早上到达时间</li>
 * <li>leaveTime:下午离开时间</li>
 * <li>noonLeaveTime:早上离开时间-四次打卡</li>
 * <li>afternoonArriveTime:下午到达时间-四次打卡</li>
 * <li>statuString：状态文字</li> 
 * </ul>
 */
public class PunchLogsDay{
 

    private String punchDay;
    private Byte punchStatus ;
	private Byte morningPunchStatus;
	private Byte afternoonPunchStatus;
    private Byte approvalStatus ;
	private Byte morningApprovalStatus;
	private Byte afternoonApprovalStatus;
    private Byte exceptionStatus ;
	private java.lang.Byte     punchTimesPerDay;
    @ItemType(PunchLogDTO.class)
    private List<PunchLogDTO> punchLogs;
    @ItemType(PunchExceptionDTO.class)
    private List<PunchExceptionDTO> punchExceptionDTOs;
    private Long workTime;
    //modify by wh 增加忘打卡 为了兼容之前版本的打卡操作 
    private Byte punchStatusNew ;
	private Byte morningPunchStatusNew;
	private Byte afternoonPunchStatusNew;
    private Byte approvalStatusNew ;
	private Byte morningApprovalStatusNew;
	private Byte afternoonApprovalStatusNew;
	//added by wh 增加申请状态
	private Byte requestFlag;
	private Byte morningRequestFlag;
	private Byte afternoonRequestFlag;
	//added by wh  2016-9-18 增加申请ID, update by tt, 160919, 改成requestToken
	private String requestToken;
	private String morningRequestToken;
	private String afternoonRequestToken;
	//added by wh 为了审批增加4个时间戳
    private Long arriveTime;
    private Long leaveTime; 
    private Long noonLeaveTime;
    private Long afternoonArriveTime;
    //added by wh 增加状态说明文字
    private String statuString;
    //added by wh 增加3.0 的字段
    private String statusList;
    private Integer punchCount;
	public Byte getMorningPunchStatus() {
		return morningPunchStatus;
	}



	public String getRequestToken() {
		return requestToken;
	}



	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}



	public String getMorningRequestToken() {
		return morningRequestToken;
	}



	public void setMorningRequestToken(String morningRequestToken) {
		this.morningRequestToken = morningRequestToken;
	}



	public String getAfternoonRequestToken() {
		return afternoonRequestToken;
	}



	public void setAfternoonRequestToken(String afternoonRequestToken) {
		this.afternoonRequestToken = afternoonRequestToken;
	}



	public void setMorningPunchStatus(Byte morningPunchStatus) {
		this.morningPunchStatus = morningPunchStatus;
	}



	public Byte getAfternoonPunchStatus() {
		return afternoonPunchStatus;
	}



	public void setAfternoonPunchStatus(Byte afternoonPunchStatus) {
		this.afternoonPunchStatus = afternoonPunchStatus;
	}



	public Byte getMorningApprovalStatus() {
		return morningApprovalStatus;
	}



	public void setMorningApprovalStatus(Byte morningApprovalStatus) {
		this.morningApprovalStatus = morningApprovalStatus;
	}



	public Byte getAfternoonApprovalStatus() {
		return afternoonApprovalStatus;
	}



	public void setAfternoonApprovalStatus(Byte afternoonApprovalStatus) {
		this.afternoonApprovalStatus = afternoonApprovalStatus;
	}

 

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }



	public String getPunchDay() {
		return punchDay;
	}



	public void setPunchDay(String punchDay) {
		this.punchDay = punchDay;
	}



	public Byte getPunchStatus() {
		return punchStatus;
	}



	public void setPunchStatus(Byte punchStatus) {
		this.punchStatus = punchStatus;
	}



	public Byte getExceptionStatus() {
		return exceptionStatus;
	}



	public void setExceptionStatus(Byte exceptionStatus) {
		this.exceptionStatus = exceptionStatus;
	}



	public Byte getApprovalStatus() {
		return approvalStatus;
	}



	public void setApprovalStatus(Byte approvalStatus) {
		this.approvalStatus = approvalStatus;
	}


 


	public java.lang.Byte getPunchTimesPerDay() {
		return punchTimesPerDay;
	}



	public void setPunchTimesPerDay(java.lang.Byte punchTimesPerDay) {
		this.punchTimesPerDay = punchTimesPerDay;
	}



	public List<PunchExceptionDTO> getPunchExceptionDTOs() {
		return punchExceptionDTOs;
	}



	public void setPunchExceptionDTOs(List<PunchExceptionDTO> punchExceptionDTOs) {
		this.punchExceptionDTOs = punchExceptionDTOs;
	}



	public List<PunchLogDTO> getPunchLogs() {
		return punchLogs;
	}



	public void setPunchLogs(List<PunchLogDTO> punchLogs) {
		this.punchLogs = punchLogs;
	}



	public Long getWorkTime() {
		return workTime;
	}



	public void setWorkTime(Long workTime) {
		this.workTime = workTime;
	}



	public Byte getPunchStatusNew() {
		return punchStatusNew;
	}



	public void setPunchStatusNew(Byte punchStatusNew) {
		this.punchStatusNew = punchStatusNew;
	}



	public Byte getMorningPunchStatusNew() {
		return morningPunchStatusNew;
	}



	public void setMorningPunchStatusNew(Byte morningPunchStatusNew) {
		this.morningPunchStatusNew = morningPunchStatusNew;
	}



	public Byte getAfternoonPunchStatusNew() {
		return afternoonPunchStatusNew;
	}



	public void setAfternoonPunchStatusNew(Byte afternoonPunchStatusNew) {
		this.afternoonPunchStatusNew = afternoonPunchStatusNew;
	}



	public Byte getApprovalStatusNew() {
		return approvalStatusNew;
	}



	public void setApprovalStatusNew(Byte approvalStatusNew) {
		this.approvalStatusNew = approvalStatusNew;
	}



	public Byte getMorningApprovalStatusNew() {
		return morningApprovalStatusNew;
	}



	public void setMorningApprovalStatusNew(Byte morningApprovalStatusNew) {
		this.morningApprovalStatusNew = morningApprovalStatusNew;
	}



	public Byte getAfternoonApprovalStatusNew() {
		return afternoonApprovalStatusNew;
	}



	public void setAfternoonApprovalStatusNew(Byte afternoonApprovalStatusNew) {
		this.afternoonApprovalStatusNew = afternoonApprovalStatusNew;
	}



	public Byte getRequestFlag() {
		return requestFlag;
	}



	public void setRequestFlag(Byte requestFlag) {
		this.requestFlag = requestFlag;
	}



	public Byte getMorningRequestFlag() {
		return morningRequestFlag;
	}



	public void setMorningRequestFlag(Byte morningRequestFlag) {
		this.morningRequestFlag = morningRequestFlag;
	}



	public Byte getAfternoonRequestFlag() {
		return afternoonRequestFlag;
	}



	public void setAfternoonRequestFlag(Byte afternoonRequestFlag) {
		this.afternoonRequestFlag = afternoonRequestFlag;
	}



	public Long getArriveTime() {
		return arriveTime;
	}



	public void setArriveTime(Long arriveTime) {
		this.arriveTime = arriveTime;
	}



	public Long getLeaveTime() {
		return leaveTime;
	}



	public void setLeaveTime(Long leaveTime) {
		this.leaveTime = leaveTime;
	}



	public Long getNoonLeaveTime() {
		return noonLeaveTime;
	}



	public void setNoonLeaveTime(Long noonLeaveTime) {
		this.noonLeaveTime = noonLeaveTime;
	}



	public Long getAfternoonArriveTime() {
		return afternoonArriveTime;
	}



	public void setAfternoonArriveTime(Long afternoonArriveTime) {
		this.afternoonArriveTime = afternoonArriveTime;
	}



	public String getStatuString() {
		return statuString;
	}



	public void setStatuString(String statuString) {
		this.statuString = statuString;
	}



	public String getStatusList() {
		return statusList;
	}



	public void setStatusList(String statusList) {
		this.statusList = statusList;
	}



	public Integer getPunchCount() {
		return punchCount;
	}



	public void setPunchCount(Integer punchCount) {
		this.punchCount = punchCount;
	}


 

 

 }
