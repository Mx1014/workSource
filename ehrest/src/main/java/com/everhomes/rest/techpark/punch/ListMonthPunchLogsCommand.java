package com.everhomes.rest.techpark.punch;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

 /**
 * <ul>  
 * <li>companyId：企业Id</li>
 * <li>queryYear：查询日期 取年份</li>
 * </ul>
 */
public class ListMonthPunchLogsCommand {

	@NotNull
	private Long     enterpriseId; 
	@NotNull
    private Long queryTime;
     
     @Override
     public String toString() {
         return StringHelper.toJsonString(this);
     }

	public Long getQueryTime() {
		return queryTime;
	}

	public void setQueryTime(Long queryTime) {
		this.queryTime = queryTime;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
 
 

 }
