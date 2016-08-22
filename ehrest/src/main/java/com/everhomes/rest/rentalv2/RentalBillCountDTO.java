package com.everhomes.rest.rentalv2;

import com.everhomes.util.StringHelper;
/**
 * <ul>统计返回值
 * <li>siteName：场所/服务 类型名 </li>
 * <li>sumCount：总数</li> 
 * <li>completeCount：已完成总数</li> 
 * <li>cancelCount：已取消总数</li> 
 * <li>overTimeCount：已过期总数</li> 
 * <li>successCount：已预约总数</li> 
 * </ul>
 */
public class RentalBillCountDTO {

	private String siteName;
	private Integer sumCount;
	private Integer completeCount;
	private Integer cancelCount;
	private Integer overTimeCount;
	private Integer successCount;
	
	@Override
    public String toString() {

		
        return StringHelper.toJsonString(this);
    }

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Integer getSumCount() {
		return sumCount;
	}

	public void setSumCount(Integer sumCount) {
		this.sumCount = sumCount;
	}

	public Integer getCompleteCount() {
		return completeCount;
	}

	public void setCompleteCount(Integer completeCount) {
		this.completeCount = completeCount;
	}

	public Integer getCancelCount() {
		return cancelCount;
	}

	public void setCancelCount(Integer cancelCount) {
		this.cancelCount = cancelCount;
	}

	public Integer getOverTimeCount() {
		return overTimeCount;
	}

	public void setOverTimeCount(Integer overTimeCount) {
		this.overTimeCount = overTimeCount;
	}

	public Integer getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}
 
}
