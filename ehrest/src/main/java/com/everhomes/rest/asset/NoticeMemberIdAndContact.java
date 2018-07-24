//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2018/5/11.
 */

public class NoticeMemberIdAndContact {
    private String contactToken;
    private Long targetId;

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
}
