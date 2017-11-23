// @formatter:off
package com.everhomes.rest.user.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.user.UserAppealLogDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>appealLogs: 申诉记录 {@link com.everhomes.rest.user.UserAppealLogDTO}</li>
 *     <li>nextPageAnchor: 下一页锚点</li>
 * </ul>
 */
public class ListUserAppealLogsResponse {

    @ItemType(UserAppealLogDTO.class)
    private List<UserAppealLogDTO> appealLogs;
    private Long nextPageAnchor;

    public List<UserAppealLogDTO> getAppealLogs() {
        return appealLogs;
    }

    public void setAppealLogs(List<UserAppealLogDTO> appealLogs) {
        this.appealLogs = appealLogs;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
