package com.everhomes.techpark.punch;

import com.everhomes.util.StringHelper;

 /**
 * <ul>打卡异常申报
 * <li>companyId：企业Id</li>
 * <li>punchDate：打卡日期</li>
 * <li>requestType：申请类型   参考{@link com.everhomes.techpark.punch.PunchRquestType}</li>
 * <li>description：申请说明</li>
 * </ul>
 */
public class PunchExceptionRequestCommand {

    private Long companyId;
    private String punchDate;
    private Byte requestType;
    private String description;
 

     public Long getCompanyId() {
         return companyId;
     }

     public void setCompanyId(Long companyId) {
         this.companyId = companyId;
     }

     @Override
     public String toString() {
         return StringHelper.toJsonString(this);
     }

	public String getPunchDate() {
		return punchDate;
	}

	public void setPunchDate(String punchDate) {
		this.punchDate = punchDate;
	}

	public Byte getRequestType() {
		return requestType;
	}

	public void setRequestType(Byte requestType) {
		this.requestType = requestType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

 }
