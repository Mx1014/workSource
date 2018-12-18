package com.everhomes.print.siyin;

import org.apache.commons.collections.CollectionUtils;

import com.everhomes.util.StringHelper;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("brocadesoft")
public class Brocadesoft {
	@XStreamAlias("job_list")
    private JobList brocadesoft;

	public JobList getBrocadesoft() {
		return brocadesoft;
	}

	public void setBrocadesoft(JobList brocadesoft) {
		this.brocadesoft = brocadesoft;
	}
	
	public boolean isEmpty() {
		return null == brocadesoft || CollectionUtils.isEmpty(brocadesoft.getJobList());
	}
	
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}