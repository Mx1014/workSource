package com.everhomes.flow;

import com.everhomes.rest.flow.FlowActionStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>flowButton: flowButton</li>
 *     <li>message: message</li>
 *     <li>sms: sms</li>
 *     <li>tracker: tracker</li>
 *     <li>script: script</li>
 * </ul>
 */
public class FlowGraphButton implements Serializable {

    private FlowButton flowButton;
    private FlowGraphAction remindMsg;
    private FlowGraphAction message;
    private FlowGraphAction sms;
    private FlowGraphAction tracker;
    private FlowGraphAction script;

    public void fireAction(FlowCaseState ctx) {
        fireAction(ctx, this.getMessage());
        fireAction(ctx, this.getRemindMsg());
        fireAction(ctx, this.getSms());
        fireAction(ctx, this.getTracker());
        fireAction(ctx, this.getScript());
    }

    private void fireAction(FlowCaseState ctx, FlowGraphAction action) {
        if (action != null) {
            Byte status = action.getFlowAction().getStatus();
            if (FlowActionStatus.fromCode(status) == FlowActionStatus.ENABLED) {
                action.fireAction(ctx, ctx.getCurrentEvent());
            }
        }
    }

    public FlowGraphAction getMessage() {
        return message;
    }

    public void setMessage(FlowGraphAction message) {
        this.message = message;
    }

    public FlowGraphAction getSms() {
        return sms;
    }

    public void setSms(FlowGraphAction sms) {
        this.sms = sms;
    }

    public FlowGraphAction getScript() {
        return script;
    }

    public void setScript(FlowGraphAction script) {
        this.script = script;
    }

    public FlowGraphAction getTracker() {
        return tracker;
    }

    public void setTracker(FlowGraphAction tracker) {
        this.tracker = tracker;
    }

    public FlowButton getFlowButton() {
        return flowButton;
    }

    public void setFlowButton(FlowButton flowButton) {
        this.flowButton = flowButton;
    }

    public FlowGraphAction getRemindMsg() {
        return remindMsg;
    }

    public void setRemindMsg(FlowGraphAction remindMsg) {
        this.remindMsg = remindMsg;
    }
}
