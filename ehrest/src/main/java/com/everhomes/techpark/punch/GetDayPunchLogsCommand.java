package com.everhomes.techpark.punch;

import com.everhomes.util.StringHelper;

 /**
 * <ul>  
 * <li>companyId：企业Id</li>
 * <li>queryDate：查询日期</li>
 * </ul>
 */
public class GetDayPunchLogsCommand {
 
    private Long companyId; 
    private String queryDate;
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
 

	public String getQueryDate() {
		return queryDate;
	}

	public void setQueryDate(String queryDate) {
		this.queryDate = queryDate;
	}
 

 }
