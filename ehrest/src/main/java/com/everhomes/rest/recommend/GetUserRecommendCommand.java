package com.everhomes.rest.recommend;

import javax.validation.constraints.NotNull;

/**
 * <ul> 获取推荐用户
 * <li>userId: 用户ID</li>
 * </ul>
 * @author janson
 *
 */
public class GetUserRecommendCommand {
    @NotNull
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
}
