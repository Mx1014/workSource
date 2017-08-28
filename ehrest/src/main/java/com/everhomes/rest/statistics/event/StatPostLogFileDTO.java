package com.everhomes.rest.statistics.event;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>sessionId: sessionId</li>
 *     <li>uri: uri</li>
 * </ul>
 */
public class StatPostLogFileDTO {

    private String sessionId;
    private String uri;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
