package com.everhomes.flow;

import com.everhomes.rest.flow.FlowEntitySel;
import com.everhomes.rest.flow.FlowEventType;
import com.everhomes.rest.flow.FlowUserType;

import java.util.List;

public interface FlowGraphEvent {
    FlowUserType getUserType();

    FlowEventType getEventType();

    Long getFiredUserId();

    Long getFiredButtonId();

    Long getNextNodeId();

    List<FlowEntitySel> getEntitySel();

    FlowSubject getSubject();

    void fire(FlowCaseState ctx);
}