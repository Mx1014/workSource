package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
* <ul>  
* <li>abnormalStatus：DELETED(0)删除状态,INVALID(1)无效状态,RUNNING(2)运行状态 {@link com.everhomes.rest.general_approval.GeneralApprovalStatus}</li>
* </ul>
*/
public class CheckAbnormalStatusResponse {
	private Byte abnormalStatus; 
	
	

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }



	public Byte getAbnormalStatus() {
		return abnormalStatus;
	}



	public void setAbnormalStatus(Byte abnormalStatus) {
		this.abnormalStatus = abnormalStatus;
	}

 

  
}
