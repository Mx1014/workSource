package com.everhomes.rest.decoration;

public class GetQrDetailCommand {

    private Long uid;
    private Long requestId;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }
}
