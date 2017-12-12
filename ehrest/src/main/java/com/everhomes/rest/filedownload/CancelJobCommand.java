// @formatter:off
package com.everhomes.rest.filedownload;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>jobId: jobId</li>
 * </ul>
 */
public class CancelJobCommand {

    private Long jobId;

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
