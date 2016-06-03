package com.everhomes.rest.quality;


import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>ownerId: 标准所属的主体id</li>
 *  <li>ownerType: 标准所属的主体，com.everhomes.rest.quality.OwnerType</li>
 *  <li>name: 任务名称</li>
 *  <li>categoryId: category表中的id</li>
 *  <li>group: 业务组信息 com.everhomes.rest.quality.StandardGroupDTO</li>
 *  <li>attachments: 核查上报内容图片</li>
 *  <li>message: 核查上报内容文字</li>
 *  <li>verificationResult: 核查结果   com.everhomes.rest.quality.QualityInspectionTaskResult</li>
 *  <li>endTime: 整改截止时间</li>
 *  <li>operatorType: 整改执行人类型</li>
 *  <li>operatorId: 整改执行人id</li>
 * </ul>
 */
public class CreateQualityInspectionTaskCommand {

	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;

	private String name;
	
	private Long categoryId;
	
	@ItemType(StandardGroupDTO.class)
	private StandardGroupDTO group;

	@NotNull
	private Byte verificationResult;
	
	@NotNull
	@ItemType(AttachmentDescriptor.class)
    private List<AttachmentDescriptor> attachments;
	
	private Long endTime;
	
	private String operatorType;
	
	private Long operatorId;
	
	private String message;
	
	
	public Long getOwnerId() {
		return ownerId;
	}


	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}


	public String getOwnerType() {
		return ownerType;
	}


	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Long getCategoryId() {
		return categoryId;
	}


	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}


	public StandardGroupDTO getGroup() {
		return group;
	}


	public void setGroup(StandardGroupDTO group) {
		this.group = group;
	}


	public Byte getVerificationResult() {
		return verificationResult;
	}


	public void setVerificationResult(Byte verificationResult) {
		this.verificationResult = verificationResult;
	}


	public List<AttachmentDescriptor> getAttachments() {
		return attachments;
	}


	public void setAttachments(List<AttachmentDescriptor> attachments) {
		this.attachments = attachments;
	}


	public Long getEndTime() {
		return endTime;
	}


	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}


	public String getOperatorType() {
		return operatorType;
	}


	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}


	public Long getOperatorId() {
		return operatorId;
	}


	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
