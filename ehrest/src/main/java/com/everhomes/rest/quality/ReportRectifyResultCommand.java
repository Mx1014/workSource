package com.everhomes.rest.quality;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>taskId: 任务id</li>
 *  <li>rectifyResult: 整改结果 11-完成 12-关闭  13-转发</li>
 *  <li>endTime: 整改截止时间</li>
 *  <li>operatorType: 整改执行人类型</li>
 *  <li>operatorId: 整改执行人id</li>
 *  <li>attachments: 整改上报内容图片</li>
 *  <li>message: 整改上报内容文字</li>
 * </ul>
 */
public class ReportRectifyResultCommand {
	
	@NotNull
	private Long taskId;
	@NotNull
	private Byte rectifyResult;
	
	private Long endTime;
	
	private String operatorType;
	
	private Long operatorId;
	
	@NotNull
	@ItemType(AttachmentDescriptor.class)
    private List<AttachmentDescriptor> attachments;
	
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Byte getRectifyResult() {
		return rectifyResult;
	}

	public void setRectifyResult(Byte rectifyResult) {
		this.rectifyResult = rectifyResult;
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

	public List<AttachmentDescriptor> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentDescriptor> attachments) {
		this.attachments = attachments;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
