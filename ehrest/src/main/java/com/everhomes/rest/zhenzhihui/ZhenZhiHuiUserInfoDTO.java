// @formatter:off
package com.everhomes.rest.zhenzhihui;

import com.everhomes.util.StringHelper;

public class ZhenZhiHuiUserInfoDTO {
    private Integer     identifyType;
    private Long     userId;
    private Long     id;
    private String     identifyToken;
    private String     email;


    public Integer getIdentifyType() {
        return identifyType;
    }


    public void setIdentifyType(Integer identifyType) {
        this.identifyType = identifyType;
    }


    public Long getUserId() {
        return userId;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getIdentifyToken() {
        return identifyToken;
    }


    public void setIdentifyToken(String identifyToken) {
        this.identifyToken = identifyToken;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
