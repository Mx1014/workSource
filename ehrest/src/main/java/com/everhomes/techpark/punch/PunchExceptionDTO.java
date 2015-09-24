package com.everhomes.techpark.punch;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

 /**
 * <ul>  
 * <li>requestTime：申请时间</li>
 * <li>description：申请内容{@link com.everhomes.techpark.punch.PunchStatus}</li>
 * <li>exceptionProcessStatus：异常处理状态：驳回，待审核，有效{@link com.everhomes.techpark.punch.ExceptionProcessStatus}</li>
 * <li>processCode：处理结果:正常，迟到等 参考{@link com.everhomes.techpark.punch.ApprovalStatus}</li>
 * <li>processDetails: 无效的时候有detail</li>
 * </ul>
 */
public class PunchExceptionDTO{
	/** <li>requestTime：申请时间</li> */
	private Long requestTime;
	/** <li>description：申请内容{@link com.everhomes.techpark.punch.PunchStatus}</li> */
	private String   description;
	/** <li>exceptionProcessStatus：异常处理状态：驳回，待审核，有效{@link com.everhomes.techpark.punch.ExceptionProcessStatus}</li> */
	private Byte     exceptionProcessStatus;
	/**  <li>processCode：处理结果:正常，迟到等 参考{@link com.everhomes.techpark.punch.ApprovalStatus}</li>*/
	private Byte     processCode;
	/**<li>processDetails: 无效的时候有detail</li>  */
	private String   processDetails;
 
 
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


	public Long getRequestTime() {
		return requestTime;
	}


	public void setRequestTime(Long requestTime) {
		this.requestTime = requestTime;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Byte getExceptionProcessStatus() {
		return exceptionProcessStatus;
	}


	public void setExceptionProcessStatus(Byte exceptionProcessStatus) {
		this.exceptionProcessStatus = exceptionProcessStatus;
	}


	public Byte getProcessCode() {
		return processCode;
	}


	public void setProcessCode(Byte processCode) {
		this.processCode = processCode;
	}


	public String getProcessDetails() {
		return processDetails;
	}


	public void setProcessDetails(String processDetails) {
		this.processDetails = processDetails;
	}
 }
