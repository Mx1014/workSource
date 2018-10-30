package com.everhomes.rest.techpark.punch.admin;

/**
 * Created by wuhan on 2017/5/10.
 */
public class TestPunchDayRefreshCommand {
    public Long getRunDate() {
        return runDate;
    }

    public void setRunDate(Long runDate) {
        this.runDate = runDate;
    }
    public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	private Long orgId;
    Long runDate;
}
