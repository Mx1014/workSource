package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *  <li>taskId: 任务id</li>
 *  <li>verificationResult: 上报结果  参考{@link com.everhomes.rest.equipment.EquipmentTaskResult}</li>
 *  <li>ownerId: 任务所属的主体id</li>
 *  <li>ownerType: 任务所属的主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>equipmentTaskReportDetails: 任务下设备检查细项 {@link com.everhomes.rest.equipment.EquipmentTaskReportDetail}</li>
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

	@ItemType(EquipmentTaskReportDetail.class)
	private  List<EquipmentTaskReportDetail>  equipmentTaskReportDetails;


	@Deprecated
	@NotNull
	@ItemType(Attachment.class)
    private List<Attachment> attachments;

	@Deprecated
	private String message;

	@Deprecated
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

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
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

	public List<EquipmentTaskReportDetail> getEquipmentTaskReportDetails() {
		return equipmentTaskReportDetails;
	}

	public void setEquipmentTaskReportDetails(List<EquipmentTaskReportDetail> equipmentTaskReportDetails) {
		this.equipmentTaskReportDetails = equipmentTaskReportDetails;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
