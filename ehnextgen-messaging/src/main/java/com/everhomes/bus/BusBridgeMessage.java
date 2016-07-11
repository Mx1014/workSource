// @formatter:off
package com.everhomes.bus;

import java.io.Serializable;

public class BusBridgeMessage implements Serializable {
    private static final long serialVersionUID = -2181292639286700771L;
    
    private String initiator;
    private String subject;
    private Object args;
    
    public BusBridgeMessage() {
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Object getArgs() {
        return args;
    }

    public void setArgs(Object args) {
        this.args = args;
    }
}
