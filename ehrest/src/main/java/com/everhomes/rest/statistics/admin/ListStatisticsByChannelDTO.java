package com.everhomes.rest.statistics.admin;

public class ListStatisticsByChannelDTO {
	
	private String channel;
	
	private Integer activeCount;
	
	private Integer registerConut;
	
	private double regRatio;
	
	private double channelActiveRatio;
	
	private double channelRegRatio;

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Integer getActiveCount() {
		return activeCount;
	}

	public void setActiveCount(Integer activeCount) {
		this.activeCount = activeCount;
	}

	public Integer getRegisterConut() {
		return registerConut;
	}

	public void setRegisterConut(Integer registerConut) {
		this.registerConut = registerConut;
	}

	public double getRegRatio() {
		return regRatio;
	}

	public void setRegRatio(double regRatio) {
		this.regRatio = regRatio;
	}

	public double getChannelActiveRatio() {
		return channelActiveRatio;
	}

	public void setChannelActiveRatio(double channelActiveRatio) {
		this.channelActiveRatio = channelActiveRatio;
	}

	public double getChannelRegRatio() {
		return channelRegRatio;
	}

	public void setChannelRegRatio(double channelRegRatio) {
		this.channelRegRatio = channelRegRatio;
	}
	

}
