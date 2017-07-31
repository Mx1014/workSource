// @formatter:off
package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * Created by xq.tian on 2017/6/27.
 */
public class UserIdentifierLogDTO {

    private String identifierToken;
    private String verificationCode;
    private Byte claimStatus;
    private Integer regionCode;

    public String getIdentifierToken() {
        return identifierToken;
    }

    public void setIdentifierToken(String identifierToken) {
        this.identifierToken = identifierToken;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Byte getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(Byte claimStatus) {
        this.claimStatus = claimStatus;
    }

    public Integer getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(Integer regionCode) {
        this.regionCode = regionCode;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
