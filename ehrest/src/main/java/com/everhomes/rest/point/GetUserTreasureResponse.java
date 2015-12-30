package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;
/**
 * 
 * @author elians
 * <ul>
 * <li>couponCount:礼卷数</li>
 * <li>topicFavoriteCount:帖子收藏数</li>
 * <li>sharedCount:分享数</li>
 * </ul>
 */
public class GetUserTreasureResponse {
    private Integer couponCount;
    private Integer topicFavoriteCount;
    private Integer sharedCount;

    public Integer getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(Integer couponCount) {
        this.couponCount = couponCount;
    }

    public Integer getTopicFavoriteCount() {
        return topicFavoriteCount;
    }

    public void setTopicFavoriteCount(Integer topicFavoriteCount) {
        this.topicFavoriteCount = topicFavoriteCount;
    }

    public Integer getSharedCount() {
        return sharedCount;
    }

    public void setSharedCount(Integer sharedCount) {
        this.sharedCount = sharedCount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
