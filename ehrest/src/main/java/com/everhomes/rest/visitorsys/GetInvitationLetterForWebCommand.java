// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>visitorToken: (必填)邀请函token</li>
 * </ul>
 */
public class GetInvitationLetterForWebCommand{
    private String visitorToken;

    public String getVisitorToken() {
        return visitorToken;
    }

    public void setVisitorToken(String visitorToken) {
        this.visitorToken = visitorToken;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
