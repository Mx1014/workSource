package com.everhomes.rest.statistics.admin;



/**
 *
 * @author Administrator
 *
 *<ul>
 *<li>startTime:开始时间,格式:YYYY-MM-DD hh:mm:ss<li>
 *<li>stopTime:结束时间,格式:YYYY-MM-DD hh:mm:ss</li>
 *<li>channelId:渠道id</li>
 *<li>cityId:城市id</li>
 *<li>communityId:小区id</li>
 *</ul>
 */
public class ListStatisticsCommand {
	
	private String startTime;
    
	private String stopTime;
    
	private Long channelId;
	
	private Long cityId;
	
	private Long communityId;
	
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStopTime() {
		return stopTime;
	}

	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	
	
}
