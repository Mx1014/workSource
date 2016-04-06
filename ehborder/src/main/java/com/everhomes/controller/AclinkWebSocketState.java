package com.everhomes.controller;

public class AclinkWebSocketState {
    private Long id;
    private String uuid;
    private String hardwareId;
    
    //Check lastTick for alive
    private Long lastTick;
    
    //Check sendingTick for resent
    private Long sendingTick;
    private Integer waitingAck;
    
    //If there are no messages, we just wait for next tick.
    private Boolean waiting;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }
    
    
}
