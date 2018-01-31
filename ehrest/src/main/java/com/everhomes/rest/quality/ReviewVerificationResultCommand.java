package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *  <li>taskId: 任务id</li>
 *  <li>reviewResult: 审阅结果 1-合格 2-不合格</li>
 *  <li>namespaceId: namespaceId</li>
 * </ul>
 */
public class ReviewVerificationResultCommand {
	@NotNull
	private Long taskId;
	@NotNull
	private Byte reviewResult;

	private Integer namespaceId;

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Byte getReviewResult() {
		return reviewResult;
	}

	public void setReviewResult(Byte reviewResult) {
		this.reviewResult = reviewResult;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
