package com.everhomes.flow;

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
    private FlowGraphAction message;
    private FlowGraphAction sms;
    private FlowGraphAction tracker;
    private List<FlowGraphAction> scripts;

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

}
