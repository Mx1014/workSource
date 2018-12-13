package com.everhomes.print.siyin;

import java.util.List;

import com.everhomes.util.StringHelper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("job_list")
public class JobList {
    @XStreamImplicit(itemFieldName="job")
    private List<Job> jobList;
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<Job> getJobList() {
		return jobList;
	}



	public void setJobList(List<Job> jobList) {
		this.jobList = jobList;
	}
}
