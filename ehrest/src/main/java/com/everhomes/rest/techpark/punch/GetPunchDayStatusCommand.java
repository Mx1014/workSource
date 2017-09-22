package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;


/**
* <ul>  
* <li>enterpriseId：long 企业Id</li> 
* <li>queryTime：long 查询时间戳-查询当下的状态不用传这个参数</li> 
* </ul>
*/
public class GetPunchDayStatusCommand {
 
    private Long enterpriseId; 
    private Long queryTime;
     @Override
     public String toString() {
         return StringHelper.toJsonString(this);
     }
 
	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getQueryTime() {
		return queryTime;
	}

	public void setQueryTime(Long queryTime) {
		this.queryTime = queryTime;
	}
 

 }
