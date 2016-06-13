package com.everhomes.rest.quality;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>taskId: 任务id</li>
 *  <li>reviewResult: 审阅结果 1-合格 2-不合格</li>
 * </ul>
 */
public class ReviewVerificationResultCommand {
	@NotNull
	private Long taskId;
	@NotNull
	private Byte reviewResult;

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
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
