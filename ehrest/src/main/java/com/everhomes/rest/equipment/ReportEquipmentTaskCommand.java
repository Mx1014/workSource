package com.everhomes.rest.equipment;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>taskId: 任务id</li>
 *  <li>verificationResult: 上报结果  参考{@link com.everhomes.rest.equipment.EquipmentTaskResult}</li>
 *  <li>attachments: 上报内容图片</li>
 *  <li>message: 上报内容文字</li>
 *  <li>itemResults: 设备参数 参考{@link com.everhomes.rest.equipment.InspectionItemResult}</li>
 *  <li>ownerId: 设备所属的主体id</li>
 *  <li>ownerType: 设备所属的主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 * </ul>
 */
public class ReportEquipmentTaskCommand {
	@NotNull
	private Long taskId;
	
	@NotNull
	private Byte verificationResult;
	
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;
	
	@NotNull
	@ItemType(AttachmentDescriptor.class)
    private List<AttachmentDescriptor> attachments;
	
	private String message;
	
	@ItemType(InspectionItemResult.class)
    private List<InspectionItemResult> itemResults; 
	
	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<InspectionItemResult> getItemResults() {
		return itemResults;
	}

	public void setItemResults(List<InspectionItemResult> itemResults) {
		this.itemResults = itemResults;
	}

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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
