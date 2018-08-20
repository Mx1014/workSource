package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>overtimeInfo:加班说明信息</li>
 * </ul>
 */
public class GetOvertimeInfoResponse {

	private String overtimeInfo;
	public GetOvertimeInfoResponse(){
		super();
	}
	public GetOvertimeInfoResponse(String overtimeInfo){
		super();
		this.overtimeInfo = overtimeInfo;
	}
	public String getOvertimeInfo() {
		return overtimeInfo;
	}
	
	public void setOvertimeInfo(String overtimeInfo) {
		this.overtimeInfo = overtimeInfo;
	}
	
	@Override
	 public String toString() {
	     return StringHelper.toJsonString(this);
	 }
}
