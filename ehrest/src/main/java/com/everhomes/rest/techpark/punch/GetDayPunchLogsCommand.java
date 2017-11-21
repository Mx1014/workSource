package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

 /**
 * <ul>  
 * <li>companyId：企业Id</li>
 * <li>queryDate：查询日期</li>
 * </ul>
 */
public class GetDayPunchLogsCommand {

    private Long enterpirseId;
	 private String queryDate;


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


	public Long getEnterpirseId() {
		return enterpirseId;
	}


	public void setEnterpirseId(Long enterpirseId) {
		this.enterpirseId = enterpirseId;
	}
 

 }
