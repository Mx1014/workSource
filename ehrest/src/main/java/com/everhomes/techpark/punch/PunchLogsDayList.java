package com.everhomes.techpark.punch;
 
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

 /**
 * <ul>  
 * <li>PunchDay：每一日的punchlist</li>
 * <li>punchStatus：打卡状态  如 迟到 早退 参考{@link com.everhomes.techpark.punch.PunchStatus}</li>
 * <li>approvalStatus：审批状态  如 迟到 早退 参考{@link com.everhomes.techpark.punch.ApprovalStatus}</li>
 * <li>exceptionStatus：异常状态:异常正常 参考{@link com.everhomes.techpark.punch.ExceptionStatus}</li>
 * <li>PunchLogs: 打卡记录list</li>
 * </ul>
 */
public class PunchLogsDayList{
 

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
    private List<PunchLogDTO> PunchLogs;
    @ItemType(PunchExceptionDTO.class)
    private List<PunchExceptionDTO> PunchExceptionDTOs;
    
    
 
 


	public Byte getMorningPunchStatus() {
		return morningPunchStatus;
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



	public List<PunchLogDTO> getPunchLogs() {
		return PunchLogs;
	}



	public void setPunchLogs(List<PunchLogDTO> punchLogs) {
		PunchLogs = punchLogs;
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



	public List<PunchExceptionDTO> getPunchExceptionDTOs() {
		return PunchExceptionDTOs;
	}



	public void setPunchExceptionDTOs(List<PunchExceptionDTO> punchExceptionDTOs) {
		PunchExceptionDTOs = punchExceptionDTOs;
	}



	public java.lang.Byte getPunchTimesPerDay() {
		return punchTimesPerDay;
	}



	public void setPunchTimesPerDay(java.lang.Byte punchTimesPerDay) {
		this.punchTimesPerDay = punchTimesPerDay;
	}


 

 }
