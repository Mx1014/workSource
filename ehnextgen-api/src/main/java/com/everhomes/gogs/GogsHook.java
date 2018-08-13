package com.everhomes.gogs;

import com.everhomes.util.StringHelper;

import java.util.List;
import java.util.Map;

/**
 * {
 *   "id": 20,
 *   "type": "gogs",
 *   "config": {
 *     "content_type": "json",
 *     "url": "http://gogs.io"
 *   },
 *   "events": [
 *     "create",
 *     "push"
 *   ],
 *   "active": true,
 *   "updated_at": "2015-08-29T11:31:22.453572732+08:00",
 *   "created_at": "2015-08-29T11:31:22.453569275+08:00"
 * }
 */
public class GogsHook {

    private Long id;
    private String type;
    private Map<String, Object> config;
    private List<String> events;
    private boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
