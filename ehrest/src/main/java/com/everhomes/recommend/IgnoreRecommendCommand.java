package com.everhomes.recommend;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;

/**
 * <ul>忽略某一类推荐
 * <li>userId: 用户ID </li>
 * <li>recommandItems: 忽略推荐的类型数组 参考 {@link com.everhomes.recommend.IgnoreRecommandItem} </li>
 * </ul>
 * @author janson
 *
 */
public class IgnoreRecommendCommand {
    @NotNull
    private Long userId;
    
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    @ItemType(IgnoreRecommandItem.class)
    public List<IgnoreRecommandItem> recommandItems;

    public List<IgnoreRecommandItem> getRecommandItems() {
        return recommandItems;
    }

    public void setRecommandItems(List<IgnoreRecommandItem> recommandItems) {
        this.recommandItems = recommandItems;
    }
    
    
}
