package com.everhomes.flow.event;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowGraphEvent;
import com.everhomes.flow.FlowSubject;
import com.everhomes.rest.flow.FlowEntitySel;
import com.everhomes.rest.flow.FlowEventType;
import com.everhomes.rest.flow.FlowUserType;

import java.util.List;

/**
 * Created by xq.tian on 2017/9/15.
 */
public abstract class AbstractFlowGraphEvent implements FlowGraphEvent {

    @Override
    public FlowUserType getUserType() {
        return null;
    }

    @Override
    public FlowEventType getEventType() {
        return null;
    }

    @Override
    public Long getFiredUserId() {
        return null;
    }

    @Override
    public Long getFiredButtonId() {
        return null;
    }

    @Override
    public Long getNextNodeId() {
        return null;
    }

    @Override
    public List<FlowEntitySel> getEntitySel() {
        return null;
    }

    @Override
    public FlowSubject getSubject() {
        return null;
    }

    @Override
    public void fire(FlowCaseState ctx) {

    }
}
