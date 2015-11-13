package com.everhomes.techpark.punch;

import com.everhomes.util.StringHelper;

 /**
 * <ul>  
 * <li>companyId：企业Id</li>
 * <li>queryYear：查询日期 取年份</li>
 * </ul>
 */
public class ListMonthPunchLogsCommand {
 
    private Long companyId; 
    private Long queryTime;
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

	public Long getQueryTime() {
		return queryTime;
	}

	public void setQueryTime(Long queryTime) {
		this.queryTime = queryTime;
	}
 
 

 }
