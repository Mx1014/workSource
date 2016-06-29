package com.everhomes.rest.user;

public class FetchPastToRecentMessageAdminCommand extends FetchPastToRecentMessageCommand {

    private Long userId;
    private Integer loginId;
    
    public FetchPastToRecentMessageAdminCommand() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getLoginId() {
        return loginId;
    }

    public void setLoginId(Integer loginId) {
        this.loginId = loginId;
    }
}


