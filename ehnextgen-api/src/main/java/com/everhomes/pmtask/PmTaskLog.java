package com.everhomes.pmtask;

import com.everhomes.server.schema.tables.pojos.EhPmTaskLogs;
import com.everhomes.util.StringHelper;

public class PmTaskLog extends EhPmTaskLogs{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
//	private String operatorName;
//	
//	private String operatorPhone;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

//	public String getOperatorName() {
//		return operatorName;
//	}
//
//	public void setOperatorName(String operatorName) {
//		this.operatorName = operatorName;
//	}
//
//	public String getOperatorPhone() {
//		return operatorPhone;
//	}
//
//	public void setOperatorPhone(String operatorPhone) {
//		this.operatorPhone = operatorPhone;
//	}
	
	
}
