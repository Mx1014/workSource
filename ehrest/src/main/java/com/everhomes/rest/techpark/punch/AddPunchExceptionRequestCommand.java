package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

 /**
 * <ul>打卡异常申报
 * <li>companyId：企业Id</li>
 * <li>punchDate：打卡日期</li>
 * <li>description：申请说明</li>
 * </ul>
 */
public class AddPunchExceptionRequestCommand {

    private Long enterpriseId;
    private String punchDate;
    private String requestDescription;
 
 

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

	public String getRequestDescription() {
		return requestDescription;
	}

	public void setRequestDescription(String requestDescription) {
		this.requestDescription = requestDescription;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
 
 
 }
