package com.everhomes.flow;

import com.everhomes.rest.flow.FlowActionStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>flowButton: flowButton</li>
 *     <li>message: message</li>
 *     <li>sms: sms</li>
 *     <li>tracker: tracker</li>
 *     <li>scripts: scripts</li>
 * </ul>
 */
public class FlowGraphButton {

    private FlowButton flowButton;
    private FlowGraphAction remindMsg;
    private FlowGraphAction message;
    private FlowGraphAction sms;
    private FlowGraphAction tracker;
    private List<FlowGraphAction> scripts;

    public void fireActions(FlowCaseState ctx) {
        if (null != this.getMessage()) {
            FlowActionStatus status = FlowActionStatus.fromCode(this.getMessage().getFlowAction().getStatus());
            if (status == FlowActionStatus.ENABLED) {
                this.getMessage().fireAction(ctx, ctx.getCurrentEvent());
            }
        }
        if (null != this.getRemindMsg()) {
            FlowActionStatus status = FlowActionStatus.fromCode(this.getRemindMsg().getFlowAction().getStatus());
            if (status == FlowActionStatus.ENABLED) {
                this.getRemindMsg().fireAction(ctx, ctx.getCurrentEvent());
            }
        }
        if (null != this.getSms()) {
            FlowActionStatus status = FlowActionStatus.fromCode(this.getSms().getFlowAction().getStatus());
            if (status == FlowActionStatus.ENABLED) {
                this.getSms().fireAction(ctx, ctx.getCurrentEvent());
            }
        }
        if (null != this.getTracker()) {
            FlowActionStatus status = FlowActionStatus.fromCode(this.getTracker().getFlowAction().getStatus());
            if (status == FlowActionStatus.ENABLED) {
                this.getTracker().fireAction(ctx, ctx.getCurrentEvent());
            }
        }
        if (null != this.getScripts()) {
            for (FlowGraphAction action : this.getScripts()) {
                FlowActionStatus status = FlowActionStatus.fromCode(action.getFlowAction().getStatus());
                if (status == FlowActionStatus.ENABLED) {
                    action.fireAction(ctx, ctx.getCurrentEvent());
                }
            }
        }
    }

    public FlowGraphButton() {
        scripts = new ArrayList<>();
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

    public List<FlowGraphAction> getScripts() {
        return scripts;
    }

    public void setScripts(List<FlowGraphAction> scripts) {
        this.scripts = scripts;
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
