package com.everhomes.rest.yellowPage;

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
* <li>createTime: 创建时间</li>
* <li>createUserName: 创建人姓名，没有的话取昵称</li>
* <li>createUserToken: 创建人电话</li>
* <li>enableRead: 是否设为申请人可见 0-不可见 1-可见</li>
* <li>enableNotifyByEmail: 是否发送邮件至服务商 0-未发送 1-已发送</li>
* <li>uploads: 附件 {@link com.everhomes.rest.yellowPage.ExtraEventAttachmentDTO}</li>
* </ul>
*  @author
*  huangmingbo 2018年5月17日
**/

public class ExtraAllianceEventDTO {
	
	private String topic;
	
	private Long timeStamp;
	
	private String address;
	
	private Long providerId;
	
	private String providerName;
	
	private String members;
	
	private String content;
	
	private Long createTimeStamp;
	
	private String createUserName;
	
	private String createUserToken;
	
	private Byte enableRead;
	
	private Byte enableNotifyByEmail;
	
	@ItemType(ExtraEventAttachmentDTO.class)
	private List<ExtraEventAttachmentDTO> uploads; 

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

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getCreateUserToken() {
		return createUserToken;
	}

	public void setCreateUserToken(String createUserToken) {
		this.createUserToken = createUserToken;
	}

	public Long getCreateTimeStamp() {
		return createTimeStamp;
	}

	public void setCreateTimeStamp(Long createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
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


}
