package com.everhomes.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>parentFlowCaseId: parentFlowCaseId</li>
 *     <li>stepType: stepType</li>
 *     <li>eventLogs: eventLogs</li>
 * </ul>
 */
public class FlowSubFlowEndDTO {

    private Long parentFlowCaseId;
    private String stepType;

    @ItemType(FlowEventLog.class)
    private List<FlowEventLog> eventLogs;

    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
    }

    public Long getParentFlowCaseId() {
        return parentFlowCaseId;
    }

    public void setParentFlowCaseId(Long parentFlowCaseId) {
        this.parentFlowCaseId = parentFlowCaseId;
    }

    public List<FlowEventLog> getEventLogs() {
        return eventLogs;
    }

    public void setEventLogs(List<FlowEventLog> eventLogs) {
        this.eventLogs = eventLogs;
    }

    public void addLog(FlowEventLog log) {
        if (this.eventLogs == null) {
            this.eventLogs = new ArrayList<>();
        }
        this.eventLogs.add(log);
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
