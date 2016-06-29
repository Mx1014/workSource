package com.everhomes.rest.user;

public class FetchRecentToPastMessageAdminCommand extends FetchRecentToPastMessageCommand {
    
    private Long userId;
    private Integer loginId;

    public FetchRecentToPastMessageAdminCommand() {
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
