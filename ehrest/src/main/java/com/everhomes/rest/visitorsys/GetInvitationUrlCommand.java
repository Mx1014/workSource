package com.everhomes.rest.visitorsys;



/**
 * <ul>
 * <li>visitorToken: (必填)邀请函token</li>
 * </ul>
 */
public class GetInvitationUrlCommand {

    private String visitorToken;

    public String getVisitorToken() {
        return visitorToken;
    }

    public void setVisitorToken(String visitorToken) {
        this.visitorToken = visitorToken;
    }
}
