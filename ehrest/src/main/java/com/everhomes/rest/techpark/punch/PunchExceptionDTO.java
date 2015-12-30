package com.everhomes.rest.techpark.punch;
 
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>Name: 谁说的</li>
 * <li>createTime：创建时间</li>
 * <li>exceptionComment:说啥了 </li>
 * <li>requestType：申报还是审批{@link com.everhomes.rest.techpark.punch.PunchRquestType}</li> 
 * <li>processCode：处理结果:正常，迟到等 参考 {@link com.everhomes.rest.techpark.punch.ApprovalStatus}</li>
 * </ul>
 */
public class PunchExceptionDTO {

	private String name;
	private Long createTime;
	 
	private String exceptionComment;
	 
	private Byte requestType;
	 
	private Byte processCode;
	  
  

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


 


	public Long getCreateTime() {
		return createTime;
	}



	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}



	public String getExceptionComment() {
		return exceptionComment;
	}



	public void setExceptionComment(String exceptionComment) {
		this.exceptionComment = exceptionComment;
	}



	public Byte getRequestType() {
		return requestType;
	}



	public void setRequestType(Byte requestType) {
		this.requestType = requestType;
	}



	public Byte getProcessCode() {
		return processCode;
	}



	public void setProcessCode(Byte processCode) {
		this.processCode = processCode;
	}





	public String getName() {
		return name;
	}





	public void setName(String name) {
		this.name = name;
	}
 
}
