package com.everhomes.recommend;

import javax.validation.constraints.NotNull;

/**
 * <ul>忽略某一类推荐
 * <li>userId: 用户ID </li>
 * <li>suggestType: 推荐类型 </li>
 * <li>sourceId: 忽略推荐的源ID </li>
 * <li>sourceType: 源类型 </li>
 * </ul>
 * @author janson
 *
 */
public class IgnoreRecommendCommand {
    @NotNull
    private Long userId;
    
    @NotNull
    private Long suggestType;
    
    @NotNull
    private Long sourceId;
    
    @NotNull
    private Long sourceType;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSuggestType() {
        return suggestType;
    }

    public void setSuggestType(Long suggestType) {
        this.suggestType = suggestType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Long getSourceType() {
        return sourceType;
    }

    public void setSourceType(Long sourceType) {
        this.sourceType = sourceType;
    }
}
