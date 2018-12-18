package com.everhomes.print.siyin;

import com.everhomes.util.StringHelper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("job")
public class Job {
    @XStreamAsAttribute
    @XStreamAlias("job_id")
    private String jobId;

    @XStreamAsAttribute
    @XStreamAlias("job_name")
    private String jobName;

    @XStreamAlias("print_time")
    @XStreamAsAttribute
    private String printTime;
   
    @XStreamAlias("total_page")
    @XStreamAsAttribute
    private String totalPage;
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
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

	public String getPrintTime() {
		return printTime;
	}

	public void setPrintTime(String printTime) {
		this.printTime = printTime;
	}

	public String getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}
}
