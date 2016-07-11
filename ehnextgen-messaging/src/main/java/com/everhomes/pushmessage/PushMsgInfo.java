package com.everhomes.pushmessage;

public class PushMsgInfo {
    private Long id;
    private Long randomId;
    
    public PushMsgInfo() {
        
    }
    
    public PushMsgInfo(Long id, Long random) {
        this.id = id;
        this.randomId = random;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getRandomId() {
        return randomId;
    }
    
    public void setRandomId(Long randomId) {
        this.randomId = randomId;
    }
    
    
}
