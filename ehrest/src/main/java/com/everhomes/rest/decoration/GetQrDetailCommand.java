package com.everhomes.rest.decoration;

/**
 * <ul>
 * <li>uid：用户id</li>
 *  <li>requestId</li>
 * </ul>
 */
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
