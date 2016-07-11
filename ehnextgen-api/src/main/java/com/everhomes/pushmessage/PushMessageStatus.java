package com.everhomes.pushmessage;

public enum PushMessageStatus {
    Processing(2), Finished(1), Ready(0);
    
    private int code;
    private PushMessageStatus(int code) {
        this.code = code;
    }
    
    public int getCode() {
        return this.code;
    }
    
    public static PushMessageStatus fromCode(int code) {
        for(PushMessageStatus t : PushMessageStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
