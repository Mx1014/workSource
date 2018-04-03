package com.everhomes.rest.user;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;
/**
 * 用户标识
 * @author elians
 *<ul>
 *<li>id:标识ID</li>
 *<li>identifierType:标识类型</li>
 *<li>identifierToken:标识,手机号或者邮箱</li>
 *<li>claimStatus:状态</li>
 *</ul>
 */
public class UserIdentifierDTO {
    private java.lang.Long     id;
    private java.lang.Byte     identifierType;
    private java.lang.String   identifierToken;
    private java.lang.Byte     claimStatus;
    //just for management
    private String verifyCode;
    private Long createTimeMs;

    public UserIdentifierDTO() {
    }

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.Byte getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(java.lang.Byte identifierType) {
        this.identifierType = identifierType;
    }

    public java.lang.String getIdentifierToken() {
        return identifierToken;
    }

    public void setIdentifierToken(java.lang.String identifierToken) {
        this.identifierToken = identifierToken;
    }

    public java.lang.Byte getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(java.lang.Byte claimStatus) {
        this.claimStatus = claimStatus;
    }
    
    
    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public Long getCreateTimeMs() {
        return createTimeMs;
    }

    public void setCreateTimeMs(Long createTimeMs) {
        this.createTimeMs = createTimeMs;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
