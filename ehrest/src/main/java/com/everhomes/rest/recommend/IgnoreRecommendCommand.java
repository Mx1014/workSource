package com.everhomes.rest.recommend;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;

/**
 * <ul>忽略某一类推荐
 * <li>userId: 用户ID </li>
 * <li>recommandItems: 忽略推荐的类型数组 参考 {@link com.everhomes.rest.recommend.IgnoreRecommandItem} </li>
 * </ul>
 * @author janson
 *
 */
public class IgnoreRecommendCommand {
    @ItemType(IgnoreRecommandItem.class)
    public List<IgnoreRecommandItem> recommendItems;

    public List<IgnoreRecommandItem> getRecommendItems() {
        return recommendItems;
    }

    public void setRecommendItems(List<IgnoreRecommandItem> recommendItems) {
        this.recommendItems = recommendItems;
    }
    
    
}
