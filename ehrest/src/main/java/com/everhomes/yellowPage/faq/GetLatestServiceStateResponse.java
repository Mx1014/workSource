package com.everhomes.yellowPage.faq;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>serviceId : 服务id，未找到时为空</li>
 * <li>serviceName : 服务名称</li>
 * <li>currentStatus : 当前状态</li>
 * <li>channelPos :当前步骤下标</li>
 * <li>channels : 泳道列表</li>
 * <li>squareInfos : 广场数据 </li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年10月23日
 */
public class GetLatestServiceStateResponse {
	private Long serviceId;
	private String serviceName;
	private String currentStatus;
	private Byte channelPos;
	private List<String> channels;
	private List<String> squareInfos;
	
	public Long getServiceId() {
		return serviceId;
	}
	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Byte getChannelPos() {
		return channelPos;
	}
	public void setChannelPos(Byte channelPos) {
		this.channelPos = channelPos;
	}
	public List<String> getChannels() {
		return channels;
	}
	public void setChannels(List<String> channels) {
		this.channels = channels;
	}
	
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public List<String> getSquareInfos() {
		return squareInfos;
	}
	public void setSquareInfos(List<String> squareInfos) {
		this.squareInfos = squareInfos;
	}
}
