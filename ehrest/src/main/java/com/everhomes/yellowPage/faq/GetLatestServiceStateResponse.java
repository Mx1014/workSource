package com.everhomes.yellowPage.faq;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>serviceId : 服务id，未找到时为空</li>
 * <li>serviceName : 服务名称</li>
 * <li>flowCaseId : 工作流id</li>
 * <li>currentStatus : 当前状态</li>
 * <li>channelPos :当前步骤下标</li>
 * <li>channels : 泳道列表</li>
 * <li>serviceListUrl : 开工单 </li>
 * <li>topFAQUrl : 常见问题 </li>
 * <li>serviceCustomerId : kefu id </li>
 * <li>phoneNumber : 热线 </li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年10月23日
 */
public class GetLatestServiceStateResponse {
	private Long serviceId;
	private String serviceName;
	private Long flowCaseId;
	private String currentStatus;
	private Byte channelPos;
	private List<String> channels;
	private String serviceListUrl;
	private String topFAQUrl;
	private Long serviceCustomerId;
	private String phoneNumber;
	
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
	public String getServiceListUrl() {
		return serviceListUrl;
	}
	public void setServiceListUrl(String serviceListUrl) {
		this.serviceListUrl = serviceListUrl;
	}
	public String getTopFAQUrl() {
		return topFAQUrl;
	}
	public void setTopFAQUrl(String topFAQUrl) {
		this.topFAQUrl = topFAQUrl;
	}
	public Long getServiceCustomerId() {
		return serviceCustomerId;
	}
	public void setServiceCustomerId(Long serviceCustomerId) {
		this.serviceCustomerId = serviceCustomerId;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Long getFlowCaseId() {
		return flowCaseId;
	}
	public void setFlowCaseId(Long flowCaseId) {
		this.flowCaseId = flowCaseId;
	}
}
