//@formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>total: total</li>
 *     <li>fail: fail</li>
 *     <li>success: success</li>
 *     <li>update: update</li>
 *     <li>jobId: 任务id，用于导出错误信息到excel时使用</li>
 * </ul>
 */
public class ImportSignupInfoResponse {

	private Integer total;
	private Integer fail;
	private Integer success;
	private Integer update;
	private Long jobId;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getFail() {
		return fail;
	}

	public void setFail(Integer fail) {
		this.fail = fail;
	}

	public Integer getSuccess() {
		return success;
	}

	public void setSuccess(Integer success) {
		this.success = success;
	}

	public Integer getUpdate() {
		return update;
	}

	public void setUpdate(Integer update) {
		this.update = update;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
