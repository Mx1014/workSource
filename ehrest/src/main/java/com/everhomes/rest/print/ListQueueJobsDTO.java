// @formatter:off
package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>jobId : 任务id</li>
 * <li>jobName : 任务名称</li>
 * <li>totalPage : 打印页数量</li>
 * <li>printTime : 任务上传时间</li>
 * <li>readerName : 刷卡器</li>
 * </ul>
 */
public class ListQueueJobsDTO {
	private String jobId;
	private String jobName;
	private String totalPage;
	private String printTime;
	private String readerName;
	
	public String getReaderName() {
		return readerName;
	}

	public void setReaderName(String readerName) {
		this.readerName = readerName;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}

	public String getPrintTime() {
		return printTime;
	}

	public void setPrintTime(String printTime) {
		this.printTime = printTime;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
