package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

 /**
 * <ul>  
 * <li>companyId：企业Id</li>
 * <li>queryYear：查询日期 取年份</li>
 * </ul>
 */
public class ListYearPunchLogsCommand {
 
    private Long enterpriseId; 
    private String queryYear;
     

     @Override
     public String toString() {
         return StringHelper.toJsonString(this);
     }

	public String getQueryYear() {
		return queryYear;
	}

	public void setQueryYear(String queryYear) {
		this.queryYear = queryYear;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
 

 }
