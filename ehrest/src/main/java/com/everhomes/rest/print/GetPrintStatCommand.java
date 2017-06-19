// @formatter:off
package com.everhomes.rest.print;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>ownerType : 打印所属类型, 参考{@link com.everhomes.rest.print.PrintOwnerType}</li>
 * <li>ownerId : 所属id</li>
 * <li>startTime : 开始统计时间</li>
 * <li>endTime : 结束统计时间</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class GetPrintStatCommand {
	private String ownerType;
	private Long ownerId;
    private Timestamp startTime;
    private Timestamp endTime;
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
    
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return StringHelper.toJsonString(this);
    }
}
