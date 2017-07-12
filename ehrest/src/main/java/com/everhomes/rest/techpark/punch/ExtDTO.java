package com.everhomes.rest.techpark.punch;

/**
 * <ul>
 * <li>name：类型名:如事假,病假,产假</li>
 * <li>timeCount：时间计数 如2小时,3天3小时等</li> 
 * </ul>
 */
public class ExtDTO {
	private String name;
	private String timeCount;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTimeCount() {
		return timeCount;
	}
	public void setTimeCount(String timeCount) {
		this.timeCount = timeCount;
	}  
	
}
