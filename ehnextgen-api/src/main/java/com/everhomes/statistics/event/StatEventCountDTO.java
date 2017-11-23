// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>totalCount: totalCount</li>
 *     <li>completedSessions: completedSessions</li>
 *     <li>uniqueUsers: uniqueUsers</li>
 * </ul>
 */
public class StatEventCountDTO {

    private Integer totalCount;
    private Integer completedSessions;
    private Integer uniqueUsers;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getCompletedSessions() {
        return completedSessions;
    }

    public void setCompletedSessions(Integer completedSessions) {
        this.completedSessions = completedSessions;
    }

    public Integer getUniqueUsers() {
        return uniqueUsers;
    }

    public void setUniqueUsers(Integer uniqueUsers) {
        this.uniqueUsers = uniqueUsers;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
