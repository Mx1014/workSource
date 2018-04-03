// @formatter:off
package com.everhomes.rest.statistics.event;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>sessionId: sessionId</li>
 *     <li>uploadStrategy: 上传策略 {@link com.everhomes.rest.statistics.event.StatLogUploadStrategyDTO}列表</li>
 * </ul>
 */
public class StatPostDeviceResponse {

    private String sessionId;
    @ItemType(StatLogUploadStrategyDTO.class)
    private List<StatLogUploadStrategyDTO> uploadStrategy;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<StatLogUploadStrategyDTO> getUploadStrategy() {
        return uploadStrategy;
    }

    public void setUploadStrategy(List<StatLogUploadStrategyDTO> uploadStrategy) {
        this.uploadStrategy = uploadStrategy;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
