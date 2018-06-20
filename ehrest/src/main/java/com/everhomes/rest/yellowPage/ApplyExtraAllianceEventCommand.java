package com.everhomes.rest.yellowPage;


import java.sql.Timestamp;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
* <ul>
* <li>flowCaseId : 工作流ID</li>
* <li>topic : 事件主题</li>
* <li>timeStamp: 事件时间</li>
* <li>address: 事件地址</li>
* <li>providerId: 服务商id</li>
* <li>providerName: 服务商名称</li>
* <li>members: 参与人员</li>
* <li>content: 事件详情</li>
* <li>uploads: 附件 {@link com.everhomes.rest.yellowPage.ExtraEventAttachmentDTO}</li>
* <li>enableRead: 是否申请人可见  0-否 1-是</li>
* <li>enableNotifyByEmail: 是否给服务商发邮件 0-否 1-是</li>
* </ul>
*  @author
*  huangmingbo 2018年5月17日
**/

public class ApplyExtraAllianceEventCommand {
	
	@NotNull
	private Long flowCaseId;
	
	@NotNull
	private String topic;
	
	@NotNull
	private Long timeStamp;
	
	private String address;
	
	
	private Long providerId;
	
	private String providerName;
	
	@NotNull
	private String members;
	
	private String content;
	
	@ItemType(ExtraEventAttachmentDTO.class)
	private List<ExtraEventAttachmentDTO> uploads; 
	
	private Byte enableRead;
	
	private Byte enableNotifyByEmail;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


	public String getMembers() {
		return members;
	}

	public void setMembers(String members) {
		this.members = members;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<ExtraEventAttachmentDTO> getUploads() {
		return uploads;
	}

	public void setUploads(List<ExtraEventAttachmentDTO> uploads) {
		this.uploads = uploads;
	}

	public Byte getEnableRead() {
		return enableRead;
	}

	public void setEnableRead(Byte enableRead) {
		this.enableRead = enableRead;
	}

	public Byte getEnableNotifyByEmail() {
		return enableNotifyByEmail;
	}

	public void setEnableNotifyByEmail(Byte enableNotifyByEmail) {
		this.enableNotifyByEmail = enableNotifyByEmail;
	}

	public Long getFlowCaseId() {
		return flowCaseId;
	}

	public void setFlowCaseId(Long flowCaseId) {
		this.flowCaseId = flowCaseId;
	}

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}


}


