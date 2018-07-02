package com.everhomes.gogs;

import com.everhomes.util.StringHelper;

import java.util.List;
import java.util.Map;

/**
 * {
 *     "type": "gogs",
 *     "config": {
 *         "url": "http://gogs.io",
 *         "content_type": "json"
 *     },
 *     "events": [
 *         "create",
 *         "delete",
 *         "fork",
 *         "push",
 *         "issues",
 *         "issue_comment",
 *         "pull_request",
 *         "release",
 *     ],
 *     "active": true
 * }
 */
public class CreateGogsHookParam {

    private String type;
    private Map<String, Object> config;
    private List<String> events;
    private boolean active;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
